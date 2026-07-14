package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

//It is impossibly hard to test LibGDX UI. Therefore, this class remains untested
final class ScreenFactory {

    private final AssetManager assetManager;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final ApplicationEvents applicationEvents;
    private final UserProfile userProfile;

    ScreenFactory(
        AssetManager assetManager,
        SpriteBatch spriteBatch,
        ShapeRenderer shapeRenderer,
        ApplicationEvents applicationEvents,
        UserProfile userProfile
    ) {
        this.assetManager = assetManager;
        this.spriteBatch = spriteBatch;
        this.shapeRenderer = shapeRenderer;
        this.applicationEvents = applicationEvents;
        this.userProfile = userProfile;
    }

    Screen loadingScreen() {
        var skin = assetManager.get(Assets.SKIN);
        var progressBar = new ProgressBar(0, 1, 0.01f, false, skin);
        return new LoadingScreen(
            new StageScreen(loadingStage(skin, progressBar)),
            assetManager,
            progressBar,
            applicationEvents,
            Assets.GAME_ASSETS
        );
    }

    Screen modeSelectionScreen() {
        var bundle = assetManager.get(Assets.GAME_BUNDLE);
        var skin = assetManager.get(Assets.SKIN);
        var modeNameLabel = label("", skin);
        var modeDescriptionLabel = label("", skin);
        modeDescriptionLabel.setWrap(true);
        var descriptionTable = new Table();
        descriptionTable.add(modeNameLabel).growX().row();
        descriptionTable.add(modeDescriptionLabel).growX();
        var table = new Table();
        var modeSelectionWidth = 4;
        table.add(
                table(
                    modeSelectionWidth,
                    modeSelectionPanel(
                        bundle,
                        skin,
                        modeSelectionWidth,
                        modeNameLabel,
                        modeDescriptionLabel
                    )
                )
            )
            .width(Value.percentWidth(0.6f, table));
        table.add(descriptionTable).width(Value.percentWidth(0.4f, table));
        return new StageScreen(stage(table));
    }

    Screen titleScreen() {
        var bundle = assetManager.get(Assets.GAME_BUNDLE);
        var skin = assetManager.get(Assets.SKIN);
        var stage = stage(
            vertical(
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

    Screen snakeSessionScreen(WorldDimensions worldDimensions, SnakeSessionFactory snakeSessionFactory) {
        return new SnakeSessionScreen(
            worldDimensions,
            shapeRenderer,
            new FitViewport(worldDimensions.width(), worldDimensions.height()),
            applicationEvents,
            snakeSessionFactory
        );
    }

    private Stage loadingStage(Skin skin, ProgressBar progressBar) {
        return stage(
            vertical(
                label(
                    assetManager.get(Assets.LOADING_BUNDLE).get("loading.name"),
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
        Label modeNameLabel,
        Label modeDescriptionLabel
    ) {
        var panel = new ArrayList<Actor>();
        for (var snakeSessionFactory : snakeSessionFactories()) {
            panel.add(
                modeSelectionButton(
                    bundle,
                    skin,
                    snakeSessionFactory,
                    modeNameLabel,
                    modeDescriptionLabel
                )
            );
        }
        do {
            panel.add(null);
        } while (panel.size() % width != width - 3);
        panel.add(new TextButton(bundle.get("modeSelection.statistics"), skin));
        panel.add(new TextButton(bundle.get("modeSelection.settings"), skin));
        panel.add(new TextButton(bundle.get("modeSelection.credits"), skin));
        return panel;
    }

    private List<SnakeSessionFactory> snakeSessionFactories() {
        return List.of(
            new ClassicSnakeSessionFactory(),
            new ViperSnakeSessionFactory()
        );
    }

    private TextButton modeSelectionButton(
        I18NBundle bundle,
        Skin skin,
        SnakeSessionFactory snakeSessionFactory,
        Label modeNameLabel,
        Label modeDescriptionLabel
    ) {
        var mode = snakeSessionFactory.mode();
        var name = bundle.get(mode.nameKey());
        var textButton = new TextButton(name, skin);
        textButton.setDisabled(!userProfile.isUnlocked(mode));
        textButton.addListener(
            new FunctionalChangeListener(
                () -> applicationEvents.publish(new ModeSelected(snakeSessionFactory))
            )
        );
        textButton.addListener(
            new FunctionalHoverListener(() -> modeNameLabel.setText(name))
        );
        textButton.addListener(
            new FunctionalHoverListener(
                () -> modeDescriptionLabel.setText(bundle.get(mode.descriptionKey()))
            )
        );
        return textButton;
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

    private Table vertical(Actor... actors) {
        return table(1, List.of(actors));
    }

    private Table table(int width, List<Actor> actors) {
        var table = new Table();
        var rowIndex = 0;
        for (var actor : actors) {
            table.add(actor).align(Align.center).growX();
            rowIndex++;
            if (rowIndex == width) {
                table.row();
                rowIndex = 0;
            }
        }
        return table;
    }
}
