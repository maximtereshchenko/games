package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class SnakeSessionFactory {

    private final ShapeRenderer shapeRenderer;

    SnakeSessionFactory(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    Dominion dominion(WorldDimensions worldDimensions) {
        var dominion = Dominion.create();
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
        return dominion;
    }

    List<System> systems(
        Dominion dominion,
        Mode mode,
        Viewport viewport
    ) {
        return List.of(
            new InputSystem(dominion),
            new TurnStartSystem(dominion, 0.125),
            new SegmentSpawningSystem(dominion),
            new NextDirectionSystem(dominion, mode),
            new LeftTurnsSystem(dominion),
            new CurrentDirectionSystem(dominion),
            new HeadMovementSystem(dominion),
            new FoodEatingSystem(dominion),
            new InitialSegmentTimerSystem(dominion, 3),
            new TimerIncrementSystem(dominion, 3),
            new TimerDecrementSystem(dominion),
            new TimerRemovalSystem(dominion),
            new SessionEndSystem(dominion),
            new FoodSpawningSystem(dominion, ThreadLocalRandom.current(), 1),
            new EventRemovalSystem(dominion),
            new RenderingSystem(
                viewport,
                shapeRenderer,
                dominion
            )
        );
    }
}
