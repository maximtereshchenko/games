package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

final class SnakeApplicationListener extends ApplicationAdapter {

    private FitViewport fitViewport;
    private Scheduler scheduler;
    private StandaloneRenderingSystem renderingSystem;

    @Override
    public void create() {
        fitViewport = new FitViewport(5, 5);
        var dominion = Dominion.create();
        dominion.createEntity(new Stopwatch());
        dominion.createEntity(
            new Head(Head.Direction.RIGHT),
            Tail.INSTANCE,
            new Point(0, 0),
            Colors.HEAD
        );
        scheduler = dominion.createScheduler();
        scheduler.schedule(new TurnStartSystem(dominion, scheduler, 0.3));
        scheduler.schedule(new HeadMovementSystem(dominion, fitViewport));
        scheduler.schedule(new AppleEatingSystem(dominion));
        scheduler.schedule(new TailRemovalSystem(dominion));
        scheduler.schedule(
            new AppleSpawningSystem(
                dominion,
                ThreadLocalRandom.current(),
                fitViewport,
                1
            )
        );
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
