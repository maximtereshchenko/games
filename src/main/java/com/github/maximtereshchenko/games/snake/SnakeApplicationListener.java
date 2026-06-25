package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

final class SnakeApplicationListener extends ApplicationAdapter {

    private FitViewport fitViewport;
    private Scheduler scheduler;
    private StandaloneRenderingSystem renderingSystem;

    @Override
    public void create() {
        fitViewport = new FitViewport(10, 10);
        var dominion = Dominion.create();
        scheduler = dominion.createScheduler();
        renderingSystem = new StandaloneRenderingSystem(
            fitViewport,
            new ShapeRenderer(),
            dominion,
            () -> ScreenUtils.clear(Color.BLACK)
        );
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
    }

    @Override
    public void render() {
        renderingSystem.render();
    }

    @Override
    public void dispose() {
        scheduler.shutDown();
    }
}
