package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

    Stage titleStage(Skin skin) {
        var bundle = assetManager.get(Assets.I18N_BUNDLE);
        var table = new Table();
        table.setFillParent(true);
        table.add(new Label(bundle.get("title.name"), skin)).row();
        table.add(new Label(bundle.get("title.continue"), skin)).row();
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(table);
        stage.addListener(
            new FunctionalInputListener(
                Input.Keys.SPACE,
                () -> applicationEvents.publish(ApplicationEvent.CONTINUED_PAST_TITLE_SCREEN)
            )
        );
        return stage;
    }
}
