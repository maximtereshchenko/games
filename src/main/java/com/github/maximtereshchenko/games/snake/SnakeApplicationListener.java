package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

import java.awt.*;
import java.util.concurrent.TimeUnit;

final class SnakeApplicationListener extends ApplicationAdapter {

    private FitViewport fitViewport;
    private Scheduler scheduler;
    private StandaloneRenderingSystem renderingSystem;

    @Override
    public void create() {
        fitViewport = new FitViewport(10, 10);
        var dominion = Dominion.create();
        var entityFactory = new EntityFactory(dominion);
        entityFactory.createStopwatch();
        entityFactory.createHead(HeadDirection.RIGHT, new Point(0, 0));
        scheduler = dominion.createScheduler();
        scheduler.schedule(new TurnBasedSystem(dominion, scheduler, entityFactory, 0.5));
        scheduler.schedule(new HeadMovementSystem(dominion, fitViewport));
        scheduler.schedule(new EventRemovalSystem(dominion));
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
        scheduler.tick(
            (long) (TimeUnit.SECONDS.toNanos(1) * Gdx.graphics.getDeltaTime())
        );
        renderingSystem.render();
    }

    @Override
    public void dispose() {
        scheduler.shutDown();
    }
}
