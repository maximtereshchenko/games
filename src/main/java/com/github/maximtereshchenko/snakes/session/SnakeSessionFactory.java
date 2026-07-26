package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.snakes.configuration.Assets;
import com.github.maximtereshchenko.snakes.configuration.Mode;

import java.util.concurrent.ThreadLocalRandom;

public final class SnakeSessionFactory {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;
    private final Assets assets;

    public SnakeSessionFactory(
        ShapeRenderer shapeRenderer,
        SpriteBatch spriteBatch,
        AssetManager assetManager,
        Assets assets
    ) {
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
        this.assets = assets;
    }

    public World world(
        Mode mode,
        EntityFactory entityFactory,
        Viewport gameViewport,
        Viewport interfaceViewport
    ) {
        var world = new World();
        for (var components : mode.entities()) {
            world.addComponents(world.createEntity(), components);
        }
        world.addSystems(
            new InputSystem(world),
            new TurnStartSystem(world, entityFactory),
            new SegmentSpawningSystem(world, entityFactory),
            new NextForwardDirectionSystem(world),
            new SessionStatisticsSystem(world),
            new CurrentForwardDirectionSystem(world),
            new HeadForwardMovementSystem(world),
            new HeadSidewaysMovementSystem(world),
            new HeadCollisionTargetSystem(world),
            new WarpSystem(world),
            new AirCounterDecrementSystem(world),
            new AirCounterRefreshSystem(world),
            new FoodEatingSystem(world, entityFactory),
            new FoodEatenCounterSystem(world),
            new SegmentTimersIncrementSystem(world),
            new TimerSystem(world),
            new SegmentRemovalSystem(world),
            new SessionEndSystem(world),
            new FoodSpawningSystem(
                world,
                entityFactory,
                ThreadLocalRandom.current(),
                1
            ),
            new HeadCollisionTargetRemovalSystem(world),
            new EventRemovalSystem(world),
            new GameRenderingSystem(
                gameViewport,
                shapeRenderer,
                world,
                mode
            ),
            new InterfaceRenderingSystem(
                interfaceViewport,
                spriteBatch,
                assetManager.get(assets.bitmapFont()),
                world,
                mode
            )
        );
        return world;
    }
}
