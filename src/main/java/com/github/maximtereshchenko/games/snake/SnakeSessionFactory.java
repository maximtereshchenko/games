package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

abstract class SnakeSessionFactory {

    final SnakeSession snakeSession(
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        WorldDimensions worldDimensions
    ) {
        var dominion = Dominion.create();
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

    abstract Mode mode();

    abstract boolean setCurrentDirection(Direction current, Direction next);

    private void createEntities(Dominion dominion, WorldDimensions worldDimensions) {
        dominion.createEntity(new Session());
        dominion.createEntity(worldDimensions);
        dominion.createEntity(new Stopwatch());
        dominion.createEntity(new InitialSegmentTimer(3));
        dominion.createEntity(new LeftTurns(0));
        dominion.createEntity(
            Head.INSTANCE,
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.RIGHT),
            new Position(11, 6),
            new Visible(Colors.HEAD)
        );
        dominion.createEntity(
            new Timer(2),
            new Position(10, 6),
            new Visible(Colors.SEGMENT)
        );
        dominion.createEntity(
            new Timer(1),
            new Position(9, 6),
            new Visible(Colors.SEGMENT)
        );
    }

    private void scheduleSystems(Dominion dominion, Scheduler scheduler) {
        List.of(
                new InputSystem(dominion),
                new TurnStartSystem(dominion, scheduler, 0.125),
                new SegmentSpawningSystem(dominion),
                new NextDirectionSystem(dominion, this::setCurrentDirection),
                new LeftTurnsSystem(dominion),
                new CurrentDirectionSystem(dominion),
                new HeadMovementSystem(dominion),
                new AppleEatingSystem(dominion),
                new InitialSegmentTimerSystem(dominion, 3),
                new TimerIncrementSystem(dominion, 3),
                new TimerDecrementSystem(dominion),
                new TimerRemovalSystem(dominion),
                new SessionEndSystem(dominion),
                new AppleSpawningSystem(dominion, ThreadLocalRandom.current(), 1),
                new EventRemovalSystem(dominion)
            )
            .forEach(scheduler::schedule);
    }
}
