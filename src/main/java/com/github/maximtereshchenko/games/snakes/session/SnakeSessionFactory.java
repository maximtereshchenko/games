package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.configuration.Assets;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.engine.system.Config;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.setProperty;

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

    public Dominion dominion(Mode mode) {
        var name = "snakes";
        setProperty(
            Config.getPropertyName(Config.SHOW_BANNER),
            Boolean.toString(false)
        );
        setProperty(
            Config.getPropertyName(name, Config.CLASS_INDEX_BIT),
            "24"
        );
        var dominion = Dominion.create(name);
        for (var components : mode.entities()) {
            dominion.createEntity(components);
        }
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
            new TurnStartSystem(dominion, entityFactory),
            new SegmentSpawningSystem(dominion, entityFactory),
            new NextForwardDirectionSystem(dominion),
            new SessionStatisticsSystem(dominion),
            new CurrentForwardDirectionSystem(dominion),
            new HeadForwardMovementSystem(dominion),
            new HeadSidewaysMovementSystem(dominion),
            new WarpSystem(dominion),
            new HeadCollisionTargetSystem(dominion),
            new AirCounterDecrementSystem(dominion),
            new AirCounterRefreshSystem(dominion),
            new FoodEatingSystem(dominion, entityFactory),
            new FoodEatenCounterSystem(dominion),
            new SegmentTimersIncrementSystem(dominion),
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
            new HeadCollisionTargetRemovalSystem(dominion),
            new GameRenderingSystem(
                gameViewport,
                shapeRenderer,
                dominion,
                mode
            ),
            new InterfaceRenderingSystem(
                interfaceViewport,
                spriteBatch,
                assetManager.get(assets.bitmapFont()),
                dominion,
                mode
            )
        );
    }
}
