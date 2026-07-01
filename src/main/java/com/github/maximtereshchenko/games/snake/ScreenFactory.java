package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

final class ScreenFactory {

    private final Supplier<Dominion> supplier;

    ScreenFactory(Supplier<Dominion> supplier) {
        this.supplier = supplier;
    }

    SnakeSessionScreen snakeSessionScreen(ShapeRenderer shapeRenderer, Runnable onSessionEnd) {
        var worldDimensions = new WorldDimensions(6, 6);
        var dominion = supplier.get();
        createEntities(dominion, worldDimensions);
        var scheduler = dominion.createScheduler();
        scheduleSystems(dominion, scheduler);
        var fitViewport = new FitViewport(worldDimensions.width(), worldDimensions.height());
        return new SnakeSessionScreen(
            fitViewport,
            dominion,
            scheduler,
            new StandaloneRenderingSystem(
                fitViewport,
                shapeRenderer,
                dominion
            ),
            onSessionEnd
        );
    }

    private void createEntities(Dominion dominion, WorldDimensions worldDimensions) {
        dominion.createEntity(new Session());
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
    }

    private void scheduleSystems(Dominion dominion, Scheduler scheduler) {
        List.of(
                new InputSystem(dominion),
                new TurnStartSystem(dominion, scheduler, 0.4),
                new SegmentSpawningSystem(dominion),
                new CurrentDirectionSystem(dominion),
                new HeadMovementSystem(dominion),
                new AppleEatingSystem(dominion),
                new InitialSegmentTimerSystem(dominion),
                new TimerDecrementSystem(dominion),
                new TimerRemovalSystem(dominion),
                new SessionEndSystem(dominion),
                new AppleSpawningSystem(dominion, ThreadLocalRandom.current(), 1),
                new EventRemovalSystem(dominion)
            )
            .forEach(scheduler::schedule);
    }
}
