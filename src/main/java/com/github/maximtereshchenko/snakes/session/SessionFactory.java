package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
        var scaledFont = new ScaledFont(assetManager.get(assets.bitmapFont()));
        world.addSystems(
            new InputSystem(world),
            new TurnStartSystem(world),
            new SegmentRemainingTurnsDecrementSystem(world),
            new DirectionIntentSystem(world),
            new LeftTurnsIncrementSystem(world),
            new DirectionIntentCommitSystem(world),
            new DirectedMovementSystem(world),
            new SidewaysMovementSystem(world),
            new WarpingSystem(world),
            new WarpedRelativeDirectionSystem(world),
            new HeadCollisionSystem(world),
            new FoodCollisionSystem(world),
            new SegmentSpawningSystem(world),
            new WorldPositionIntentCommitSystem(world),
            new FoodConsumptionSystem(world),
            new FoodConsumedIncrementSystem(world),
            new WallSpawningSystem(world),
            new WarpingEdgeSpawningSystem(world),
            new FoodGrowthIncrementSystem(world),
            new FoodWarpingSystem(world),
            new FoodRemovalSystem(world),
            new ConstantAmountFoodSpawningSystem(world, ThreadLocalRandom.current()),
            new SegmentRemainingTurnsIncrementSystem(world),
            new AirSupplyDecrementSystem(world),
            new AirSupplyResetSystem(world),
            new TurnLengthScalingSystem(world),
            new FoodOpacitySynchronisationSystem(world),
            new AirSupplyInterfaceSynchronisationSystem(world),
            new FoodConsumedInterfaceElementSynchronisationSystem(world),
            new LocalizationSystem(
                world,
                assetManager.get(assets.gameBundle())
            ),
            new InterfaceTextCenterAlignmentSystem(
                world,
                interfaceViewport,
                scaledFont,
                new GlyphLayout()
            ),
            new TagRemovalSystem(
                world,
                Initializing.class,
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
                scaledFont,
                mode
            )
        );
        return world;
    }
}
