package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

final class SnakeApplicationListener extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(10, 10);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, 10, 10);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(1, 1, 1, 1);
        shapeRenderer.rect(2, 1, 1, 1);
        shapeRenderer.rect(3, 1, 1, 1);
        shapeRenderer.end();
    }
}
