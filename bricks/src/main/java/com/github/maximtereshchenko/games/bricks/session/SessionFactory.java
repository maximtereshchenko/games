package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Registry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public final class SessionFactory {

    private final ShapeRenderer shapeRenderer;
    private final FixtureFactory fixtureFactory;

    public SessionFactory(
        ShapeRenderer shapeRenderer,
        FixtureFactory fixtureFactory
    ) {
        this.shapeRenderer = shapeRenderer;
        this.fixtureFactory = fixtureFactory;
    }

    public World world(Viewport viewport) {
        var world = new World(Vector2.Zero, true);
        var shape = new ChainShape();
        shape.createChain(
            new float[]{
                0, 0,
                0, viewport.getWorldHeight(),
                viewport.getWorldWidth(), viewport.getWorldHeight(),
                viewport.getWorldWidth(), 0
            }
        );
        fixtureFactory.fixture(
            world,
            BodyDef.BodyType.StaticBody,
            new Vector2(),
            shape,
            false,
            0
        );
        shape.dispose();
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
        registry.addSystems(
            new InputSystem(registry, viewport),
            new PhysicsSynchronizationSystem(registry),
            new PhysicsSystem(world),
            new RegistrySynchronizationSystem(registry),
            new SpeedNormalizationSystem(registry),
            new PaddleCollisionSystem(registry),
            new BrickCollisionSystem(registry),
            new BonusCollisionSystem(registry),
            new BonusSpawningSystem(registry, ThreadLocalRandom.current()),
            new PaddleWideningSystem(registry),
            new WidthResettingSystem(registry),
            new BarrierSpawningSystem(registry, viewport),
            new BarrierTimeExtendingSystem(registry),
            new BarrierRemovalSystem(registry),
            new BallMultiplicationSystem(registry),
            new BallSpawningSystem(registry),
            new PhysicsResizingSystem(registry, world, fixtureFactory),
            new EntityRemovalSystem(registry, world),
            new PhysicsRegistrationSystem(
                registry,
                world,
                fixtureFactory
            ),
            new ComponentRemovalSystem(
                registry,
                BodyDef.BodyType.class,
                Sensor.class,
                CollisionGroupIndex.class,
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
        var paddleVector2 = new Vector2(
            viewport.getWorldWidth() / 2,
            2
        );
        var ballCircle = new Circle(0.1f);
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            BodyDef.BodyType.KinematicBody,
            paddleRectangle,
            new MaxWidth(4),
            new BaseWidth(paddleRectangle.halfWidth * 2),
            new ResetWidthRemainingTime(0),
            new WorldPosition(paddleVector2),
            new Velocity(new Vector2()),
            new Visible(Color.valueOf("#ff7f50"))
        );
        registry.addComponents(
            registry.createEntity(),
            Ball.INSTANCE,
            BodyDef.BodyType.DynamicBody,
            new CollisionGroupIndex(-1),
            ballCircle,
            new WorldPosition(
                new Vector2(
                    paddleVector2.x,
                    paddleVector2.y + (paddleRectangle.halfHeight + ballCircle.radius()) * 1.1f
                )
            ),
            new Velocity(new Vector2(0, 5)),
            new Speed(5),
            new Visible(Color.valueOf("#feffff"))
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
