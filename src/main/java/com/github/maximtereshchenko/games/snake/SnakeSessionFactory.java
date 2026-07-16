package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class SnakeSessionFactory {

    private final Configuration configuration;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;
    private final Assets assets;

    SnakeSessionFactory(
        Configuration configuration,
        ShapeRenderer shapeRenderer,
        SpriteBatch spriteBatch,
        AssetManager assetManager,
        Assets assets
    ) {
        this.configuration = configuration;
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
        this.assets = assets;
    }

    Dominion dominion(WorldDimensions worldDimensions) {
        var dominion = Dominion.create();
        dominion.createEntity(new Session());
        dominion.createEntity(worldDimensions);
        dominion.createEntity(new Stopwatch());
        dominion.createEntity(new InitialSegmentTimer(configuration.snakeLength()));
        dominion.createEntity(new LeftTurnsCounter(0));
        dominion.createEntity(new FoodEatenCounter(0), Colored.FOOD_EATEN_COUNTER);
        createSnake(dominion);
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
            new TurnStartSystem(dominion, mode.gameInterval()),
            new SegmentSpawningSystem(dominion),
            new NextDirectionSystem(dominion, mode),
            new LeftTurnsCounterSystem(dominion),
            new CurrentDirectionSystem(dominion),
            new HeadMovementSystem(dominion),
            new FoodEatingSystem(dominion),
            new FoodEatenCounterSystem(dominion),
            new InitialSegmentTimerSystem(dominion, configuration.snakeFoodGrowth()),
            new TimerIncrementSystem(dominion, configuration.snakeFoodGrowth()),
            new TimerDecrementSystem(dominion),
            new TimerRemovalSystem(dominion),
            new SessionEndSystem(dominion),
            new FoodSpawningSystem(dominion, ThreadLocalRandom.current(), 1),
            new EventRemovalSystem(dominion),
            new GameRenderingSystem(
                gameViewport,
                shapeRenderer,
                dominion,
                mode.palette()
            ),
            new InterfaceRenderingSystem(
                interfaceViewport,
                spriteBatch,
                assetManager.get(assets.bitmapFont()),
                dominion,
                mode.palette()
            )
        );
    }

    private void createSnake(Dominion dominion) {
        var snakeHeadPosition = configuration.snakeHeadPosition();
        dominion.createEntity(
            Head.INSTANCE,
            new CurrentDirection(configuration.snakeHeadDirection()),
            new NextDirection(configuration.snakeHeadDirection()),
            snakeHeadPosition,
            Colored.HEAD
        );
        var x = snakeHeadPosition.x;
        var y = snakeHeadPosition.y;
        var segments = configuration.snakeLength() - 1;
        for (var i = 0; i < segments; i++) {
            switch (configuration.snakeHeadDirection()) {
                case UP -> y -= 1;
                case DOWN -> y += 1;
                case LEFT -> x += 1;
                case RIGHT -> x -= 1;
            }
            dominion.createEntity(
                new Timer(segments - i),
                new Position(x, y),
                Colored.SEGMENT
            );
        }
    }
}
