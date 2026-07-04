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
        var stage = stage(
            label("title.name"),
            label("title.continue")
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
            label("loading.name"),
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

    private Label label(String key) {
        return new Label(
            assetManager.get(Assets.I18N_BUNDLE).get(key),
            assetManager.get(Assets.SKIN)
        );
    }
}
