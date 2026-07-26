package com.github.maximtereshchenko.snakes.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.maximtereshchenko.snakes.UserProfile;
import com.github.maximtereshchenko.snakes.configuration.Assets;
import com.github.maximtereshchenko.snakes.configuration.Mode;
import com.github.maximtereshchenko.snakes.event.*;
import com.github.maximtereshchenko.snakes.screen.view.CreditsView;
import com.github.maximtereshchenko.snakes.screen.view.LoadingView;
import com.github.maximtereshchenko.snakes.screen.view.StatisticsView;
import com.github.maximtereshchenko.snakes.screen.view.TitleView;
import com.github.maximtereshchenko.snakes.screen.view.main.InformationView;
import com.github.maximtereshchenko.snakes.screen.view.main.MainView;
import com.github.maximtereshchenko.snakes.screen.view.main.ModesView;
import com.github.maximtereshchenko.snakes.screen.view.main.NavigationView;
import com.github.maximtereshchenko.snakes.screen.view.settings.SettingsView;
import com.github.maximtereshchenko.snakes.session.EntityFactory;
import com.github.maximtereshchenko.snakes.session.SnakeSessionFactory;

import java.util.List;
import java.util.Locale;
import java.util.Set;

//It is impossibly hard to test LibGDX UI. Therefore, this class remains untested
public final class ScreenFactory {

    private final AssetManager assetManager;
    private final Assets assets;
    private final SpriteBatch spriteBatch;
    private final ApplicationEvents applicationEvents;
    private final UserProfile userProfile;
    private final SnakeSessionFactory snakeSessionFactory;
    private final EntityFactory entityFactory;
    private final List<Mode> modes;

    public ScreenFactory(
        AssetManager assetManager,
        Assets assets,
        SpriteBatch spriteBatch,
        ApplicationEvents applicationEvents,
        UserProfile userProfile,
        SnakeSessionFactory snakeSessionFactory,
        EntityFactory entityFactory,
        List<Mode> modes
    ) {
        this.assetManager = assetManager;
        this.assets = assets;
        this.spriteBatch = spriteBatch;
        this.applicationEvents = applicationEvents;
        this.userProfile = userProfile;
        this.snakeSessionFactory = snakeSessionFactory;
        this.entityFactory = entityFactory;
        this.modes = modes;
    }

    public Screen loadingScreen() {
        var loadingView = new LoadingView(
            assetManager.get(assets.loadingBundle()),
            assetManager.get(assets.skin())
        );
        return new LoadingScreen(
            new StageScreen(stage(loadingView)),
            loadingView,
            assetManager,
            applicationEvents,
            assets
        );
    }

    public Screen titleScreen() {
        var stage = stage(
            new TitleView(
                assetManager.get(assets.gameBundle()),
                assetManager.get(assets.skin())
            )
        );
        stage.addListener(
            new InputListener() {

                @Override
                public boolean touchDown(
                    InputEvent event,
                    float x,
                    float y,
                    int pointer,
                    int button
                ) {
                    return keyDown(event, button);
                }

                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    applicationEvents.publish(new TitleScreenFinished());
                    return true;
                }
            }
        );
        return new StageScreen(stage);
    }

    public Screen mainScreen() {
        var bundle = assetManager.get(assets.gameBundle());
        var skin = assetManager.get(assets.skin());
        var informationView = new InformationView(skin);
        return new StageScreen(
            stage(
                new MainView(
                    modesView(bundle, skin, informationView),
                    navigationView(bundle, skin, informationView),
                    informationView
                )
            )
        );
    }

    public Screen snakeSessionScreen(Mode mode) {
        var dominion = snakeSessionFactory.dominion(mode);
        var worldDimensions = mode.worldDimensions();
        var gameViewport = new FitViewport(worldDimensions.width(), worldDimensions.height());
        var interfaceViewport = new FitViewport(
            mode.interfaceViewportHeight() * worldDimensions.width() / worldDimensions.height(),
            mode.interfaceViewportHeight()
        );
        return new SnakeSessionScreen(
            Set.of(gameViewport, interfaceViewport),
            applicationEvents,
            dominion,
            snakeSessionFactory.systems(
                dominion,
                entityFactory,
                mode,
                gameViewport,
                interfaceViewport
            )
        );
    }

    public Screen statisticsScreen() {
        var statisticsView = new StatisticsView(
            assetManager.get(assets.gameBundle()),
            assetManager.get(assets.skin()),
            userProfile
        );
        statisticsView.onFinish(
            () -> applicationEvents.publish(new StatisticsScreenFinished())
        );
        return new StageScreen(stage(statisticsView));
    }

    public Screen settingsScreen() {
        var music = assetManager.get(assets.music());
        var settingsView = new SettingsView(
            assetManager.get(assets.gameBundle()),
            assetManager.get(assets.skin()),
            music.getVolume()
        );
        settingsView.onVolumeChange(volume -> setVolume(music, volume));
        settingsView.onFinish(
            () -> applicationEvents.publish(new StatisticsScreenFinished())
        );
        return new StageScreen(stage(settingsView));
    }

    public Screen creditsScreen() {
        var creditsView = new CreditsView(
            assetManager.get(assets.gameBundle()),
            assetManager.get(assets.skin())
        );
        creditsView.onFinish(
            () -> applicationEvents.publish(new CreditsScreenFinished())
        );
        return new StageScreen(stage(creditsView));
    }

    private NavigationView navigationView(
        I18NBundle bundle,
        Skin skin,
        InformationView informationView
    ) {
        var navigationView = new NavigationView(bundle, skin);
        navigationView.onHover(
            (button, target) -> informationView.update(
                button.getText().toString(),
                bundle.get(
                    "screens.main.buttons.%s.description"
                        .formatted(target.toString().toLowerCase(Locale.ROOT))
                )
            )
        );
        navigationView.onClick(
            target -> applicationEvents.publish(
                switch (target) {
                    case STATISTICS -> new StatisticsRequested();
                    case SETTINGS -> new SettingsRequested();
                    case CREDITS -> new CreditsRequested();
                }
            )
        );
        return navigationView;
    }

    private ModesView modesView(
        I18NBundle bundle,
        Skin skin,
        InformationView informationView
    ) {
        var modesView = new ModesView(bundle, skin, modes, userProfile);
        modesView.onHover(
            modeButton -> informationView.update(
                modeButton.getText().toString(),
                bundle.get(key(modeButton.mode()))
            )
        );
        modesView.onClick(
            modeButton -> applicationEvents.publish(new ModeSelected(modeButton.mode()))
        );
        return modesView;
    }

    private String key(Mode mode) {
        if (userProfile.isUnlocked(mode)) {
            return "modes.%s.description".formatted(mode.name());
        }
        return "modes.%s.unlock.requirement".formatted(mode.name());
    }

    private void setVolume(Music music, float volume) {
        music.setVolume(volume);
        userProfile.updateMusicVolume(volume);
    }

    private Stage stage(Table table) {
        table.setFillParent(true);
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(table);
        return stage;
    }
}
