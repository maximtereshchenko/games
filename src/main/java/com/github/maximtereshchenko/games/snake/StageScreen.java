package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

abstract class StageScreen extends ScreenAdapter {

    private final Stage stage;

    StageScreen(Stage stage) {
        this.stage = stage;
    }

    @Override
    public final void render(float delta) {
        beforeRendering();
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public final void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public final void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public final void hide() {
        Gdx.input.setInputProcessor(null);
    }

    abstract void beforeRendering();
}
