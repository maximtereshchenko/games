package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.snakes.configuration.Assets;
import com.github.maximtereshchenko.snakes.configuration.ConfigurationReader;
import com.github.maximtereshchenko.snakes.configuration.Mode;

import java.util.concurrent.ThreadLocalRandom;

public final class SessionFactory {

    private final ConfigurationReader configurationReader;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;
    private final Assets assets;

    public SessionFactory(
        ConfigurationReader configurationReader,
        ShapeRenderer shapeRenderer,
        SpriteBatch spriteBatch,
        AssetManager assetManager,
        Assets assets
    ) {
        this.configurationReader = configurationReader;
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
        this.assets = assets;
    }

    public World world(
        Mode mode,
        Viewport gameViewport,
        Viewport interfaceViewport
    ) {
        var world = new World();
        for (var components : configurationReader.entities(mode)) {
            world.addComponents(world.createEntity(), components);
        }
        var bitmapFont = assetManager.get(assets.bitmapFont());
        world.addSystems(
            new InputSystem(world),
            new TurnStartSystem(world),
            new DirectionIntentSystem(world),
            new LeftTurnsIncrementSystem(world),
            new DirectionIntentCommitSystem(world),
            new DirectedMovementSystem(world),
            new SidewaysMovementSystem(world),
            new WarpSystem(world),
            new WarpedRelativeDirectionSystem(world),
            new HeadCollisionSystem(world),
            new FoodCollisionSystem(world),
            new SegmentSpawningSystem(world),
            new PositionIntentCommitSystem(world),
            new FoodConsumptionSystem(world),
            new FoodConsumedIncrementSystem(world),
            new FoodSpawningSystem(world, ThreadLocalRandom.current()),
            new SegmentRemainingTurnsIncrementSystem(world),
            new SegmentRemainingTurnsDecrementSystem(world),
            new AirSupplyDecrementSystem(world),
            new AirSupplyResetSystem(world),
            new AirSupplyInterfaceElementSynchronisationSystem(
                world,
                assetManager.get(assets.gameBundle())
            ),
            new FoodConsumedInterfaceElementSynchronisationSystem(world),
            new InterfaceTextCenterAlignmentSystem(
                world,
                interfaceViewport,
                bitmapFont
            ),
            new TagRemovalSystem(
                world,
                TurnStarted.class,
                FoodConsumed.class,
                Warped.class
            ),
            new WorldRenderingSystem(
                world,
                gameViewport,
                shapeRenderer,
                mode
            ),
            new InterfaceRenderingSystem(
                world,
                interfaceViewport,
                spriteBatch,
                bitmapFont,
                mode
            )
        );
        return world;
    }
}
