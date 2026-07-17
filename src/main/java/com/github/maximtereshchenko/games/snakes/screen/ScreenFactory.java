package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.maximtereshchenko.games.snakes.*;
import com.github.maximtereshchenko.games.snakes.event.*;
import com.github.maximtereshchenko.games.snakes.session.SnakeSessionFactory;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

//It is impossibly hard to test LibGDX UI. Therefore, this class remains untested
public final class ScreenFactory {

    private final Configuration configuration;
    private final AssetManager assetManager;
    private final Assets assets;
    private final SpriteBatch spriteBatch;
    private final ApplicationEvents applicationEvents;
    private final UserProfile userProfile;
    private final SnakeSessionFactory snakeSessionFactory;
    private final List<Mode> modes;

    public ScreenFactory(
        Configuration configuration,
        AssetManager assetManager,
        Assets assets,
        SpriteBatch spriteBatch,
        ApplicationEvents applicationEvents,
        UserProfile userProfile,
        SnakeSessionFactory snakeSessionFactory,
        List<Mode> modes
    ) {
        this.configuration = configuration;
        this.assetManager = assetManager;
        this.assets = assets;
        this.spriteBatch = spriteBatch;
        this.applicationEvents = applicationEvents;
        this.userProfile = userProfile;
        this.snakeSessionFactory = snakeSessionFactory;
        this.modes = modes;
    }

    public Screen loadingScreen() {
        var skin = assetManager.get(assets.skin());
        var progressBar = new ProgressBar(0, 1, 0.01f, false, skin);
        return new LoadingScreen(
            new StageScreen(loadingStage(skin, progressBar)),
            assetManager,
            progressBar,
            applicationEvents,
            assets
        );
    }

    public Screen modeSelectionScreen() {
        var bundle = assetManager.get(assets.gameBundle());
        var skin = assetManager.get(assets.skin());
        var titleLabel = label("", skin);
        var descriptionLabel = label("", skin);
        descriptionLabel.setWrap(true);
        var descriptionTable = new Table();
        descriptionTable.add(titleLabel).growX().row();
        descriptionTable.add(descriptionLabel).growX();
        var table = new Table();
        var modeSelectionWidth = 4;
        table.add(
                table(
                    modeSelectionWidth,
                    modeSelectionPanel(
                        bundle,
                        skin,
                        modeSelectionWidth,
                        titleLabel,
                        descriptionLabel
                    )
                )
            )
            .width(Value.percentWidth(0.6f, table));
        table.add(descriptionTable).width(Value.percentWidth(0.4f, table));
        return new StageScreen(stage(table));
    }

    public Screen titleScreen() {
        var bundle = assetManager.get(assets.gameBundle());
        var skin = assetManager.get(assets.skin());
        var stage = stage(
            table(
                1,
                label(bundle.get("title.name"), skin),
                label(bundle.get("title.continue"), skin)
            )
        );
        stage.addListener(
            new FunctionalInputListener(
                Input.Keys.SPACE,
                () -> applicationEvents.publish(new TitleScreenFinished())
            )
        );
        return new StageScreen(stage);
    }

    public Screen snakeSessionScreen(
        WorldDimensions worldDimensions,
        Mode mode
    ) {
        var dominion = snakeSessionFactory.dominion(worldDimensions);
        var gameViewport = new FitViewport(worldDimensions.width(), worldDimensions.height());
        var interfaceViewport = new FitViewport(
            configuration.interfaceViewportHeight() * worldDimensions.width() / worldDimensions.height(),
            configuration.interfaceViewportHeight()
        );
        return new SnakeSessionScreen(
            Set.of(gameViewport, interfaceViewport),
            applicationEvents,
            dominion,
            snakeSessionFactory.systems(
                dominion,
                mode,
                gameViewport,
                interfaceViewport
            )
        );
    }

    public Screen statisticsScreen() {
        var bundle = assetManager.get(assets.gameBundle());
        var skin = assetManager.get(assets.skin());
        var textButton = new TextButton(bundle.get("statistics.back"), skin);
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new StatisticsScreenFinished())
            )
        );
        return new StageScreen(
            stage(
                table(
                    1,
                    table(
                        2,
                        Stream.of(UserProfileStatistics.values())
                            .map(
                                userProfileStatistics -> List.of(
                                    label(bundle.get("statistics." + userProfileStatistics), skin),
                                    label(String.valueOf(userProfile.value(userProfileStatistics)), skin)
                                )
                            )
                            .flatMap(Collection::stream)
                            .toList()
                    ),
                    textButton
                )
            )
        );
    }

    public Screen creditsScreen() {
        var bundle = assetManager.get(assets.gameBundle());
        var skin = assetManager.get(assets.skin());
        var textButton = new TextButton(bundle.get("credits.back"), skin);
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new CreditsScreenFinished())
            )
        );
        return new StageScreen(
            stage(
                table(
                    1,
                    label(bundle.get("credits.text"), skin),
                    textButton
                )
            )
        );
    }

    public Screen settingsScreen() {
        var bundle = assetManager.get(assets.gameBundle());
        var skin = assetManager.get(assets.skin());
        var music = assetManager.get(assets.music());
        var textButton = new TextButton(bundle.get("settings.back"), skin);
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new SettingsScreenFinished())
            )
        );
        var musicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        musicVolumeSlider.setValue(music.getVolume());
        musicVolumeSlider.addListener(
            new FunctionalChangeListener(() -> setVolume(musicVolumeSlider, music))
        );
        return new StageScreen(
            stage(
                table(
                    1,
                    table(
                        2,
                        label(bundle.get("settings.music.volume"), skin),
                        musicVolumeSlider
                    ),
                    textButton
                )
            )
        );
    }

    private void setVolume(Slider musicVolumeSlider, Music music) {
        userProfile.updateMusicVolume(musicVolumeSlider.getValue());
        music.setVolume(musicVolumeSlider.getValue());
    }

    private Stage loadingStage(Skin skin, ProgressBar progressBar) {
        return stage(
            table(
                1,
                label(
                    assetManager.get(assets.loadingBundle()).get("loading.name"),
                    skin
                ),
                progressBar
            )
        );
    }

    private List<Actor> modeSelectionPanel(
        I18NBundle bundle,
        Skin skin,
        int width,
        Label titleLabel,
        Label descriptionLabel
    ) {
        var panel = new ArrayList<Actor>();
        for (var mode : modes) {
            panel.add(
                modeSelectionButton(
                    bundle,
                    skin,
                    mode,
                    titleLabel,
                    descriptionLabel
                )
            );
        }
        do {
            panel.add(null);
        } while (panel.size() % width != width - 3);
        panel.add(statisticsButton(bundle, skin, titleLabel, descriptionLabel));
        panel.add(settingsButton(bundle, skin, titleLabel, descriptionLabel));
        panel.add(creditsButton(bundle, skin, titleLabel, descriptionLabel));
        return panel;
    }

    private TextButton settingsButton(
        I18NBundle bundle,
        Skin skin,
        Label titleLabel,
        Label descriptionLabel
    ) {
        var settingsName = bundle.get("modeSelection.settings.name");
        var textButton = new TextButton(settingsName, skin);
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new SettingsRequested())
            )
        );
        textButton.addListener(
            new FunctionalHoverListener(() -> titleLabel.setText(settingsName))
        );
        textButton.addListener(
            new FunctionalHoverListener(
                () -> descriptionLabel.setText(
                    bundle.get("modeSelection.settings.description")
                )
            )
        );
        return textButton;
    }

    private TextButton creditsButton(
        I18NBundle bundle,
        Skin skin,
        Label titleLabel,
        Label descriptionLabel
    ) {
        var creditsName = bundle.get("modeSelection.credits.name");
        var textButton = new TextButton(creditsName, skin);
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new CreditsRequested())
            )
        );
        textButton.addListener(
            new FunctionalHoverListener(() -> titleLabel.setText(creditsName))
        );
        textButton.addListener(
            new FunctionalHoverListener(
                () -> descriptionLabel.setText(
                    bundle.get("modeSelection.credits.description")
                )
            )
        );
        return textButton;
    }

    private TextButton statisticsButton(
        I18NBundle bundle,
        Skin skin,
        Label titleLabel,
        Label descriptionLabel
    ) {
        var statisticsName = bundle.get("modeSelection.statistics.name");
        var textButton = new TextButton(statisticsName, skin);
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new StatisticsRequested())
            )
        );
        textButton.addListener(
            new FunctionalHoverListener(() -> titleLabel.setText(statisticsName))
        );
        textButton.addListener(
            new FunctionalHoverListener(
                () -> descriptionLabel.setText(
                    bundle.get("modeSelection.statistics.description")
                )
            )
        );
        return textButton;
    }

    private TextButton modeSelectionButton(
        I18NBundle bundle,
        Skin skin,
        Mode mode,
        Label titleLabel,
        Label descriptionLabel
    ) {
        var name = bundle.get("mode.%s.name".formatted(mode.name()));
        var textButton = new TextButton(name, skin);
        textButton.setDisabled(!userProfile.isUnlocked(mode));
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new ModeSelected(mode))
            )
        );
        textButton.addListener(
            new FunctionalHoverListener(() -> titleLabel.setText(name))
        );
        textButton.addListener(
            new FunctionalHoverListener(
                () -> descriptionLabel.setText(bundle.get(key(mode)))
            )
        );
        return textButton;
    }

    private String key(Mode mode) {
        if (userProfile.isUnlocked(mode)) {
            return "mode.%s.description".formatted(mode.name());
        }
        return "mode.%s.requirement".formatted(mode.name());
    }

    private Label label(String text, Skin skin) {
        var label = new Label(text, skin);
        label.setAlignment(Align.center);
        return label;
    }

    private Stage stage(Table table) {
        table.setFillParent(true);
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(table);
        return stage;
    }

    private Table table(int width, Actor... actors) {
        return table(width, List.of(actors));
    }

    private Table table(int width, List<? extends Actor> actors) {
        var table = new Table();
        table.pad(5);
        var rowIndex = 0;
        for (var actor : actors) {
            table.add(actor).align(Align.center).growX().pad(5);
            rowIndex++;
            if (rowIndex == width) {
                table.row();
                rowIndex = 0;
            }
        }
        return table;
    }
}
