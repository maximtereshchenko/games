package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.event.EventBus;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public final class SessionFactory {

    private final ShapeRenderer shapeRenderer;
    private final EventBus<Event> eventBus;
    private final PhysicsObjectFactory physicsObjectFactory;
    private final Blueprints blueprints;

    public SessionFactory(
        ShapeRenderer shapeRenderer,
        EventBus<Event> eventBus,
        PhysicsObjectFactory physicsObjectFactory,
        Blueprints blueprints
    ) {
        this.shapeRenderer = shapeRenderer;
        this.eventBus = eventBus;
        this.physicsObjectFactory = physicsObjectFactory;
        this.blueprints = blueprints;
    }

    public World world(Viewport viewport) {
        var world = new World(Vector2.Zero, true);
        physicsObjectFactory.createBoundaries(
            world,
            viewport
        );
        return world;
    }

    public Registry registry(Viewport viewport, World world) {
        var registry = new Registry();
        registry.addComponents(
            registry.createEntity(),
            blueprints.components(
                BricksBlueprints.PADDLE,
                new WorldPosition(
                    new Vector2(
                        viewport.getWorldWidth() / 2,
                        2
                    )
                )
            )
        );
        createBricks(registry);
        addSystems(registry, world, viewport);
        return registry;
    }

    private void addSystems(
        Registry registry,
        World world,
        Viewport viewport
    ) {
        var random = ThreadLocalRandom.current();
        registry.addSystems(
            new PaddleMovementSystem(registry, viewport),
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
            new StarIncrementingSystem(registry),
            new RectangleResizingSystem(
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
                BodyDef.BodyType.class,
                Sensor.class,
                CollisionGroupIndex.class,
                Attaching.class,
                Collisions.class,
                Resized.class
            ),
            new WorldRenderingSystem(
                registry,
                viewport,
                shapeRenderer
            )
        );
    }

    private void createBricks(Registry registry) {
        var brickRectangle = new Rectangle(0.1f, 0.1f);
        var sideColors = List.of(
            "#f7f7ed",
            "#fee883",
            "#f6d15f",
            "#fb9a0f",
            "#f78000",
            "#f24209",
            "#f78000"
        );
        var colors = Stream.of(
                sideColors,
                List.of("#fb9a0f"),
                sideColors.reversed()
            )
            .flatMap(Collection::stream)
            .toList();
        var x = 0.625f;
        var startY = 6f;
        for (var color : colors) {
            for (var i = 0; i < 27; i++) {
                registry.addComponents(
                    registry.createEntity(),
                    Brick.INSTANCE,
                    BodyDef.BodyType.StaticBody,
                    brickRectangle,
                    new WorldPosition(new Vector2(x, startY + 0.1f + 0.3f * i)),
                    new Visible(Color.valueOf(color))
                );
            }
            x += 0.625f;
        }
    }
}
