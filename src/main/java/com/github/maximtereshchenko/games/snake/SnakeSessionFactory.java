package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class SnakeSessionFactory {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;

    SnakeSessionFactory(
        ShapeRenderer shapeRenderer,
        SpriteBatch spriteBatch,
        AssetManager assetManager
    ) {
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
    }

    Dominion dominion(WorldDimensions worldDimensions) {
        var dominion = Dominion.create();
        dominion.createEntity(new Session());
        dominion.createEntity(worldDimensions);
        dominion.createEntity(new Stopwatch());
        dominion.createEntity(new InitialSegmentTimer(3));
        dominion.createEntity(new LeftTurnsCounter(0));
        dominion.createEntity(new FoodEatenCounter(0));
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
        Viewport gameViewport,
        Viewport interfaceViewport
    ) {
        return List.of(
            new InputSystem(dominion),
            new TurnStartSystem(dominion, 0.125),
            new SegmentSpawningSystem(dominion),
            new NextDirectionSystem(dominion, mode),
            new LeftTurnsCounterSystem(dominion),
            new CurrentDirectionSystem(dominion),
            new HeadMovementSystem(dominion),
            new FoodEatingSystem(dominion),
            new FoodEatenCounterSystem(dominion),
            new InitialSegmentTimerSystem(dominion, 3),
            new TimerIncrementSystem(dominion, 3),
            new TimerDecrementSystem(dominion),
            new TimerRemovalSystem(dominion),
            new SessionEndSystem(dominion),
            new FoodSpawningSystem(dominion, ThreadLocalRandom.current(), 1),
            new EventRemovalSystem(dominion),
            new GameRenderingSystem(
                gameViewport,
                shapeRenderer,
                dominion
            ),
            new InterfaceRenderingSystem(
                interfaceViewport,
                spriteBatch,
                assetManager.get(Assets.BITMAP_FONT),
                dominion
            )
        );
    }
}
