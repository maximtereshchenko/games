package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Registry;

public final class SessionFactory {

    private final ShapeRenderer shapeRenderer;

    public SessionFactory(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public Registry registry(Viewport viewport, World physicsWorld) {
        var registry = new Registry();
        var paddleRectangle = new Rectangle(2.07f, 0.21f);
        var paddleVector2 = new Vector2(
            viewport.getWorldWidth() / 2,
            1.87f
        );
        var ballCircle = new Circle(0.1f);
        var brickRectangle = new Rectangle(0.22f, 0.22f);
        var brickVisible = new Visible(Color.valueOf("#f24209"));
        for (var x = 0.39f; x <= 9.8f; x += brickRectangle.width + 0.45f) {
            for (var y = 5.58f; y <= 14.35f; y += brickRectangle.height + 0.12f) {
                registry.addComponents(
                    registry.createEntity(),
                    Brick.INSTANCE,
                    new PhysicsSpecification(BodyDef.BodyType.StaticBody),
                    brickRectangle,
                    new WorldPosition(new Vector2(x + brickRectangle.width / 2, y + brickRectangle.height / 2)),
                    brickVisible
                );
            }
        }
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new PhysicsSpecification(BodyDef.BodyType.KinematicBody),
            paddleRectangle,
            new WorldPosition(paddleVector2),
            new Velocity(new Vector2()),
            new Visible(Color.valueOf("#ff7f50"))
        );
        registry.addComponents(
            registry.createEntity(),
            Ball.INSTANCE,
            new PhysicsSpecification(BodyDef.BodyType.DynamicBody),
            ballCircle,
            new WorldPosition(
                new Vector2(
                    paddleVector2.x,
                    paddleVector2.y + paddleRectangle.height / 2 + ballCircle.radius()
                )
            ),
            new Velocity(new Vector2(0, 5)),
            new Visible(Color.valueOf("#feffff"))
        );
        registry.addSystems(
            new PhysicsSpecificationSystem(registry, physicsWorld),
            new InputSystem(registry, viewport),
            new PhysicsSynchronizationSystem(registry),
            new PhysicsSystem(physicsWorld),
            new WorldSynchronizationSystem(registry),
            new PaddleCollisionSystem(registry),
            new BrickCollisionSystem(registry),
            new BrickRemovalSystem(registry, physicsWorld),
            new ComponentRemovalSystem(registry, PhysicsSpecification.class, Collision.class),
            new WorldRenderingSystem(registry, viewport, shapeRenderer, physicsWorld)
        );
        return registry;
    }

    public World physicsWorld(Viewport viewport) {
        var physicsWorld = new World(Vector2.Zero, true);
        var bodyDefinition = new BodyDef();
        bodyDefinition.type = BodyDef.BodyType.StaticBody;
        var shape = new ChainShape();
        shape.createChain(
            new float[]{
                0, 0,
                0, viewport.getWorldHeight(),
                viewport.getWorldWidth(), viewport.getWorldHeight(),
                viewport.getWorldWidth(), 0
            }
        );
        var fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.friction = 0;
        fixtureDefinition.restitution = 1;
        physicsWorld.createBody(bodyDefinition)
            .createFixture(fixtureDefinition);
        shape.dispose();
        return physicsWorld;
    }
}
