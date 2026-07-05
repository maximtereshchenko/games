package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

final class StageFactory {

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
        var stage = stage(
            label(bundle.get("title.name")),
            label(bundle.get("title.continue"))
        );
        stage.addListener(
            new FunctionalInputListener(
                Input.Keys.SPACE,
                () -> applicationEvents.publish(ApplicationEvent.CONTINUED_PAST_TITLE_SCREEN)
            )
        );
        return stage;
    }

    Stage loadingStage(ProgressBar progressBar) {
        return stage(
            label(assetManager.get(Assets.LOADING_BUNDLE).get("loading.name")),
            progressBar
        );
    }

    private Stage stage(Actor... actors) {
        var table = new Table();
        table.setFillParent(true);
        for (var actor : actors) {
            table.add(actor).row();
        }
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(table);
        return stage;
    }

    private Label label(String text) {
        return new Label(
            text,
            assetManager.get(Assets.SKIN)
        );
    }
}
