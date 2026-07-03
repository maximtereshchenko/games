package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

final class SnakeSessionFactory {

    private final Supplier<Dominion> supplier;

    SnakeSessionFactory(Supplier<Dominion> supplier) {
        this.supplier = supplier;
    }

    SnakeSession snakeSession(
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        WorldDimensions worldDimensions
    ) {
        var dominion = supplier.get();
        createEntities(dominion, worldDimensions);
        var scheduler = dominion.createScheduler();
        scheduleSystems(dominion, scheduler);
        return new SnakeSession(
            dominion,
            scheduler,
            new StandaloneRenderingSystem(
                viewport,
                shapeRenderer,
                dominion
            )
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
