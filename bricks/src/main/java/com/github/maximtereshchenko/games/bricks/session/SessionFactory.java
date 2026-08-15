package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Registry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public final class SessionFactory {

    private final ShapeRenderer shapeRenderer;
    private final PhysicsObjectFactory physicsObjectFactory;

    public SessionFactory(
        ShapeRenderer shapeRenderer,
        PhysicsObjectFactory physicsObjectFactory
    ) {
        this.shapeRenderer = shapeRenderer;
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

    public Registry registry(Viewport viewport, World world) {
        var registry = new Registry();
        createCommon(registry, viewport);
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
            new BallResettingSystem(registry),
            new PhysicsSynchronizationSystem(registry),
            new PhysicsSystem(world),
            new RegistrySynchronizationSystem(registry),
            new SpeedNormalizationSystem(registry),
            new PaddleCollisionSystem(registry),
            new BrickCollisionSystem(registry),
            new BonusCollisionSystem(registry),
            new BonusSpawningSystem(registry, random),
            new StarSpawningSystem(registry, random),
            new PaddleWideningSystem(registry),
            new WidthResettingSystem(registry),
            new BarrierSpawningSystem(registry, viewport),
            new BarrierTimeExtendingSystem(registry),
            new BarrierRemovalSystem(registry),
            new BallMultiplicationSystem(registry),
            new BallSpawningSystem(registry),
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

    private void createCommon(Registry registry, Viewport viewport) {
        var paddleRectangle = new Rectangle(1, 0.1f);
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            BodyDef.BodyType.KinematicBody,
            paddleRectangle,
            new MaxWidth(4),
            new BaseWidth(paddleRectangle.halfWidth * 2),
            new ResetWidthRemainingTime(0),
            new WorldPosition(
                new Vector2(
                    viewport.getWorldWidth() / 2,
                    2
                )
            ),
            new Velocity(new Vector2()),
            new SpawnedStars(3, 0),
            new StarCounter(0),
            new Lives(3),
            new Visible(Color.valueOf("#ff7f50"))
        );
        registry.addComponents(
            registry.createEntity(),
            new BonusSpawnPolicy(
                0.05f,
                List.of(
                    List.of(
                        new WidenPaddle(0.5f, 10),
                        new Visible(Color.GREEN)
                    ),
                    List.of(
                        new SpawnBarrier(10),
                        new Visible(Color.valueOf("#ff9859"))
                    ),
                    List.of(
                        new MultiplyBalls(3),
                        new Visible(Color.valueOf("#a6d81d"))
                    ),
                    List.of(
                        new SpawnBalls(3),
                        new Visible(Color.valueOf("#00cce4"))
                    )
                )
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
