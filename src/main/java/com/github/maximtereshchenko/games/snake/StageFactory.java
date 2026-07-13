package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

//It is impossibly hard to test LibGDX UI. Therefore, this class remains untested
final class StageFactory {

    static final String ASSETS_LOADING_BAR = "ASSETS_LOADING_BAR";

    private final AssetManager assetManager;
    private final SpriteBatch spriteBatch;
    private final ApplicationEvents applicationEvents;

    StageFactory(
        AssetManager assetManager,
        SpriteBatch spriteBatch,
        ApplicationEvents applicationEvents
    ) {
        this.assetManager = assetManager;
        this.spriteBatch = spriteBatch;
        this.applicationEvents = applicationEvents;
    }

    Stage titleStage() {
        var bundle = assetManager.get(Assets.GAME_BUNDLE);
        var skin = assetManager.get(Assets.SKIN);
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
        return stage;
    }

    Stage loadingStage() {
        var skin = assetManager.get(Assets.SKIN);
        var progressBar = new ProgressBar(0, 1, 0.01f, false, skin);
        progressBar.setName(ASSETS_LOADING_BAR);
        return stage(
            table(
                1,
                label(
                    assetManager.get(Assets.LOADING_BUNDLE).get("loading.name"),
                    skin
                ),
                progressBar
            )
        );
    }

    Stage modeSelectionStage(List<SnakeSessionFactory> snakeSessionFactories) {
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
                        snakeSessionFactories,
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
        return stage(table);
    }

    private List<Actor> modeSelectionPanel(
        List<SnakeSessionFactory> snakeSessionFactories,
        I18NBundle bundle,
        Skin skin,
        int width,
        Label modeNameLabel,
        Label modeDescriptionLabel
    ) {
        var panel = new ArrayList<Actor>();
        for (var snakeSessionFactory : snakeSessionFactories) {
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
        textButton.addListener(
            new FunctionalClickListener(
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

    private Table table(int width, Actor... actors) {
        return table(width, List.of(actors));
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
