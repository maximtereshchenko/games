package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
                new Label(bundle.get("title.name"), skin),
                new Label(bundle.get("title.continue"), skin)
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
                new Label(
                    assetManager.get(Assets.LOADING_BUNDLE).get("loading.name"),
                    skin
                ),
                progressBar
            )
        );
    }

    Stage modeSelectionStage(LinkedHashMap<Mode, SnakeSessionFactory> modes) {
        var bundle = assetManager.get(Assets.GAME_BUNDLE);
        var skin = assetManager.get(Assets.SKIN);
        var firstMode = modes.firstEntry().getKey();
        var modeNameLabel = new Label(bundle.get(firstMode.nameKey()), skin);
        var modeDescriptionLabel = new Label(bundle.get(firstMode.descriptionKey()), skin);
        var modeSelectionWidth = 4;
        return stage(
            table(
                2,
                table(
                    modeSelectionWidth,
                    modeSelectionPanel(
                        modes,
                        bundle,
                        skin,
                        modeSelectionWidth
                    )
                ),
                table(
                    1,
                    modeNameLabel,
                    modeDescriptionLabel
                )
            )
        );
    }

    private List<Actor> modeSelectionPanel(
        LinkedHashMap<Mode, SnakeSessionFactory> modes,
        I18NBundle bundle,
        Skin skin,
        int width
    ) {
        var panel = new ArrayList<Actor>();
        for (var entry : modes.entrySet()) {
            panel.add(
                modeSelectionButton(
                    bundle,
                    skin,
                    entry.getKey(),
                    entry.getValue()
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
        Mode mode,
        SnakeSessionFactory snakeSessionFactory
    ) {
        var textButton = new TextButton(bundle.get(mode.nameKey()), skin);
        textButton.addListener(
            new FunctionalClickListener(
                () -> applicationEvents.publish(new ModeSelected(snakeSessionFactory))
            )
        );
        return textButton;
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
            table.add(actor);
            rowIndex++;
            if (rowIndex == width) {
                table.row();
                rowIndex = 0;
            }
        }
        return table;
    }
}
