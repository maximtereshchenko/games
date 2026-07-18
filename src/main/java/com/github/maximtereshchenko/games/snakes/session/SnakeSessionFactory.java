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

    public Dominion dominion(EntityFactory entityFactory, WorldDimensions worldDimensions) {
        var dominion = Dominion.create();
        entityFactory.createGlobals(dominion, worldDimensions);
        createSnake(dominion, entityFactory, worldDimensions);
        return dominion;
    }

    public List<System> systems(
        Dominion dominion,
        EntityFactory entityFactory,
        Mode mode,
        Viewport gameViewport,
        Viewport interfaceViewport
    ) {
        return List.of(
            new InputSystem(dominion),
            new TurnStartSystem(
                dominion,
                entityFactory,
                mode.gameInterval()
            ),
            new SegmentSpawningSystem(dominion, entityFactory),
            new NextForwardDirectionSystem(dominion, mode),
            new SessionStatisticsSystem(dominion),
            new CurrentForwardDirectionSystem(dominion),
            new HeadForwardMovementSystem(dominion),
            new FoodEatingSystem(dominion, entityFactory),
            new FoodEatenCounterSystem(dominion),
            new InitialSegmentTimerSystem(
                dominion,
                configuration.snakeFoodGrowth()
            ),
            new SegmentTimerIncrementSystem(
                dominion,
                configuration.snakeFoodGrowth()
            ),
            new TimerSystem(dominion),
            new SegmentRemovalSystem(dominion),
            new SessionEndSystem(dominion),
            new FoodSpawningSystem(
                dominion,
                entityFactory,
                ThreadLocalRandom.current(),
                1
            ),
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

    private void createSnake(
        Dominion dominion,
        EntityFactory entityFactory,
        WorldDimensions worldDimensions
    ) {
        entityFactory.createHead(dominion);
        var position = new Position(configuration.snakeHeadPosition());
        var positionDirection = configuration.snakeHeadForwardDirection().opposite();
        var segments = configuration.snakeLength() - 1;
        for (var i = 0; i < segments; i++) {
            position.move(worldDimensions, positionDirection);
            entityFactory.createSegment(dominion, position, segments - i);
            position = new Position(position);
        }
    }
}
