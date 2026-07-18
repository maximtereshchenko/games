package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.Assets;
import com.github.maximtereshchenko.games.snakes.Configuration;
import com.github.maximtereshchenko.games.snakes.Mode;
import dev.dominion.ecs.api.Dominion;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class SnakeSessionFactory {

    private final Configuration configuration;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;
    private final Assets assets;

    public SnakeSessionFactory(
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

    public Dominion dominion(WorldDimensions worldDimensions) {
        var dominion = Dominion.create();
        dominion.createEntity(new Session());
        dominion.createEntity(worldDimensions);
        dominion.createEntity(new Stopwatch());
        dominion.createEntity(new InitialSegmentTimer(configuration.snakeLength()));
        dominion.createEntity(new SessionStatisticsAccumulator());
        dominion.createEntity(new FoodEatenCounter(0), Colored.FOOD_EATEN_COUNTER);
        createSnake(worldDimensions, dominion);
        return dominion;
    }

    public List<System> systems(
        Dominion dominion,
        Mode mode,
        Viewport gameViewport,
        Viewport interfaceViewport
    ) {
        return List.of(
            new InputSystem(dominion),
            new TurnStartSystem(dominion, mode.gameInterval()),
            new SegmentSpawningSystem(dominion),
            new NextForwardDirectionSystem(dominion, mode),
            new SessionStatisticsSystem(dominion),
            new CurrentForwardDirectionSystem(dominion),
            new HeadForwardMovementSystem(dominion),
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

    private void createSnake(WorldDimensions worldDimensions, Dominion dominion) {
        var snakeHeadPosition = configuration.snakeHeadPosition();
        var snakeHeadForwardDirection = configuration.snakeHeadForwardDirection();
        dominion.createEntity(
            Head.INSTANCE,
            new CurrentForwardDirection(snakeHeadForwardDirection),
            new NextForwardDirection(snakeHeadForwardDirection),
            snakeHeadPosition,
            Colored.HEAD
        );
        var previous = new Position(snakeHeadPosition);
        var positionDirection = snakeHeadForwardDirection.opposite();
        var segments = configuration.snakeLength() - 1;
        for (var i = 0; i < segments; i++) {
            previous.move(worldDimensions, positionDirection);
            dominion.createEntity(
                new Timer(segments - i),
                previous,
                Colored.SEGMENT
            );
            previous = new Position(previous);
        }
    }
}
