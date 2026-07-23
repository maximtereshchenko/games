package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.configuration.Assets;
import com.github.maximtereshchenko.games.snakes.configuration.Configuration;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
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

    public Dominion dominion(Mode mode) {
        var dominion = Dominion.create();
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
            new NextForwardDirectionSystem(dominion, mode),
            new SessionStatisticsSystem(dominion),
            new CurrentForwardDirectionSystem(dominion),
            new HeadForwardMovementSystem(dominion),
            new HeadSidewaysMovementSystem(dominion),
            new WarpSystem(dominion),
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
