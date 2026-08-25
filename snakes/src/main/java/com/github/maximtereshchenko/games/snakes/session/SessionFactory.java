package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.snakes.configuration.Assets;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import tools.jackson.core.type.TypeReference;

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

    public Registry registry(
        Mode mode,
        Viewport gameViewport,
        Viewport interfaceViewport
    ) {
        var registry = new Registry();
        for (var components : entities(mode)) {
            registry.addComponents(registry.createEntity(), components);
        }
        var scaledFont = new ScaledFont(assetManager.get(assets.bitmapFont()));
        var random = ThreadLocalRandom.current();
        registry.addSystems(
            new InputSystem(registry),
            new TurnStartSystem(registry),
            new SegmentRemainingTurnsDecrementSystem(registry),
            new DirectionIntentSystem(registry),
            new LeftTurnsIncrementSystem(registry),
            new DirectionIntentCommitSystem(registry),
            new DirectedMovementSystem(registry),
            new SidewaysMovementSystem(registry),
            new WarpingSystem(registry),
            new WarpedRelativeDirectionSystem(registry),
            new WarpsIncrementSystem(registry),
            new HeadCollisionSystem(registry),
            new FoodCollisionSystem(registry),
            new SegmentSpawningSystem(registry),
            new WorldPositionIntentCommitSystem(registry),
            new FoodConsumptionSystem(registry),
            new FoodConsumedIncrementSystem(registry),
            new WallSpawningSystem(registry),
            new WarpingEdgeSpawningSystem(registry),
            new FoodGrowthIncrementSystem(registry),
            new FoodWarpingSystem(registry),
            new FoodRemovalSystem(registry),
            new ConstantAmountFoodSpawningSystem(registry, random),
            new WallClusterFoodSpawningSystem(registry, random),
            new SegmentRemainingTurnsIncrementSystem(registry),
            new AirSupplyDecrementSystem(registry),
            new AirSupplyResetSystem(registry),
            new TurnLengthScalingSystem(registry),
            new FoodOpacitySynchronisationSystem(registry),
            new AirSupplyInterfaceSynchronisationSystem(registry),
            new FoodConsumedInterfaceElementSynchronisationSystem(registry),
            new LocalizationSystem(
                registry,
                assetManager.get(assets.gameBundle())
            ),
            new InterfaceTextCenterAlignmentSystem(
                registry,
                interfaceViewport,
                scaledFont,
                new GlyphLayout()
            ),
            new TagRemovalSystem(
                registry,
                Initializing.class,
                TurnStarted.class,
                FoodConsumed.class,
                Warped.class
            ),
            new WorldRenderingSystem(
                registry,
                gameViewport,
                shapeRenderer,
                mode
            ),
            new InterfaceRenderingSystem(
                registry,
                interfaceViewport,
                spriteBatch,
                scaledFont,
                mode
            )
        );
        return registry;
    }

    private Object[][] entities(Mode mode) {
        return configurationReader.value(
            mode.entities(),
            new TypeReference<>() {}
        );
    }
}
