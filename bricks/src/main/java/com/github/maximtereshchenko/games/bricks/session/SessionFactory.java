package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.bricks.configuration.CellDefinition;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.event.EventBus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class SessionFactory {

    private final ShapeRenderer shapeRenderer;
    private final EventBus<Event> eventBus;
    private final PhysicsObjectFactory physicsObjectFactory;

    public SessionFactory(
        ShapeRenderer shapeRenderer,
        EventBus<Event> eventBus,
        PhysicsObjectFactory physicsObjectFactory
    ) {
        this.shapeRenderer = shapeRenderer;
        this.eventBus = eventBus;
        this.physicsObjectFactory = physicsObjectFactory;
    }

    public World world(Viewport viewport) {
        var world = new World(Vector2.Zero, true);
        physicsObjectFactory.createBoundaries(
            world,
            viewport
        );
        return world;
    }

    public Registry registry(
        Viewport viewport,
        Blueprints blueprints,
        List<List<CellDefinition>> cellDefinitions,
        World world
    ) {
        var registry = new Registry();
        var random = ThreadLocalRandom.current();
        registry.addSystems(
            new PaddleSpawningSystem(registry, blueprints),
            new LayoutSystem(
                registry,
                cellDefinitions,
                blueprints,
                viewport
            ),
            new PaddleMovementSystem(registry, viewport),
            new BallDetachingSystem(registry),
            new BallLaunchingSystem(registry, world),
            new BonusResettingSystem(registry),
            new BallResettingSystem(registry, blueprints),
            new PhysicsSynchronizationSystem(registry),
            new PhysicsSystem(world),
            new RegistrySynchronizationSystem(registry),
            new SpeedNormalizationSystem(registry),
            new PaddleCollisionSystem(registry),
            new BrickCollisionSystem(registry),
            new BonusCollisionSystem(registry),
            new BonusSpawningSystem(
                registry,
                blueprints,
                random
            ),
            new StarSpawningSystem(
                registry,
                blueprints,
                random
            ),
            new PaddleWideningSystem(registry),
            new PaddleShorteningSystem(registry),
            new WidthResettingSystem(registry),
            new BarrierSpawningSystem(registry, blueprints),
            new BarrierTimeExtendingSystem(registry),
            new BarrierRemovalSystem(registry),
            new BallMultiplicationSystem(
                registry,
                blueprints
            ),
            new BallSpawningSystem(registry, blueprints),
            new LifeIncrementingSystem(registry),
            new DecrementLivesBonusSystem(registry),
            new StarIncrementingSystem(registry),
            new FixtureWidthUpdatingSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new OutOfBoundsEntityRemovalSystem(
                registry,
                viewport
            ),
            new FixtureRemovalSystem(registry, world),
            new RectangleFixtureSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new CircleFixtureSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new SensorFixtureSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new PaddleJointSystem(
                registry,
                world,
                physicsObjectFactory
            ),
            new BallLossLifeDecrementingSystem(registry),
            new LifeDecrementingSystem(registry),
            new LevelFailedPublishingSystem(
                registry,
                eventBus
            ),
            new LevelCompletedPublishingSystem(
                registry,
                eventBus
            ),
            new ComponentRemovalSystem(
                registry,
                LayoutPolicy.class,
                BodyDef.BodyType.class,
                Sensor.class,
                CollisionGroupIndex.class,
                Collisions.class,
                UpdateWidthCommand.class,
                DecrementLivesCommand.class
            ),
            new WorldRenderingSystem(
                registry,
                viewport,
                shapeRenderer
            )
        );
        return registry;
    }
}
