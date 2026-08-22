package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.screen.view.Root;

final class StageScreen extends ScreenAdapter {

    private final Stage stage;

    StageScreen(
        Configuration configuration,
        Root root,
        SpriteBatch spriteBatch
    ) {
        var dimensions = configuration.interfaceDimensions();
        this.stage = new Stage(
            new FitViewport(
                dimensions.width(),
                dimensions.height()
            ),
            spriteBatch
        );
        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
