package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

final class SnakeApplicationListener extends ApplicationAdapter {

    private FitViewport fitViewport;
    private Dominion dominion;
    private Scheduler scheduler;
    private StandaloneRenderingSystem standaloneRenderingSystem;

    @Override
    public void create() {
        var worldDimensions = new WorldDimensions(6, 6);
        fitViewport = new FitViewport(worldDimensions.width(), worldDimensions.height());
        dominion = Dominion.create();
        dominion.createEntity(new Game());
        dominion.createEntity(worldDimensions);
        dominion.createEntity(new Stopwatch());
        dominion.createEntity(new InitialSegmentTimer(1));
        dominion.createEntity(
            Head.INSTANCE,
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.RIGHT),
            new Position(0, 0),
            new Visible(Colors.HEAD)
        );
        scheduler = dominion.createScheduler();
        scheduler.schedule(new InputSystem(dominion));
        scheduler.schedule(new TurnStartSystem(dominion, scheduler, 0.4));
        scheduler.schedule(new SegmentSpawningSystem(dominion));
        scheduler.schedule(new CurrentDirectionSystem(dominion));
        scheduler.schedule(new HeadMovementSystem(dominion));
        scheduler.schedule(new AppleEatingSystem(dominion));
        scheduler.schedule(new InitialSegmentTimerSystem(dominion));
        scheduler.schedule(new TimerDecrementSystem(dominion));
        scheduler.schedule(new TimerRemovalSystem(dominion));
        scheduler.schedule(new GameEndSystem(dominion));
        scheduler.schedule(new AppleSpawningSystem(dominion, ThreadLocalRandom.current(), 1));
        scheduler.schedule(new EventRemovalSystem(dominion));
        standaloneRenderingSystem = new StandaloneRenderingSystem(
            fitViewport,
            new ShapeRenderer(),
            dominion
        );
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
    }

    @Override
    public void render() {
        for (var game : dominion.findCompositionsWith(Game.class)) {
            if (game.status == Game.Status.ENDED) {
                return;
            }
        }
        scheduler.tick(
            (long) (TimeUnit.SECONDS.toNanos(1) * Gdx.graphics.getDeltaTime())
        );
        standaloneRenderingSystem.render();
    }

    @Override
    public void dispose() {
        scheduler.shutDown();
        standaloneRenderingSystem.dispose();
    }
}
