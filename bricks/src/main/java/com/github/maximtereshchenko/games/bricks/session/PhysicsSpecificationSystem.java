package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.*;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PhysicsSpecificationSystem implements System {

    private final Iterable<Entity> rectangleEntities;
    private final Iterable<Entity> circleEntities;
    private final World physicsWorld;

    PhysicsSpecificationSystem(
        Registry registry,
        World physicsWorld
    ) {
        this.rectangleEntities = registry.entities(
            new Query().all(PhysicsSpecification.class, WorldPosition.class, Rectangle.class)
        );
        this.circleEntities = registry.entities(
            new Query()
                .all(
                    PhysicsSpecification.class,
                    WorldPosition.class,
                    Velocity.class,
                    Circle.class
                )
        );
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : rectangleEntities) {
            var physicsSpecification = entity.component(PhysicsSpecification.class);
            var worldPosition = entity.component(WorldPosition.class);
            var rectangle = entity.component(Rectangle.class);
            var bodyDefinition = new BodyDef();
            bodyDefinition.type = physicsSpecification.bodyType();
            bodyDefinition.position.set(worldPosition.vector2());
            var shape = new PolygonShape();
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2);
            var fixtureDefinition = new FixtureDef();
            fixtureDefinition.shape = shape;
            fixtureDefinition.density = 1;
            fixtureDefinition.friction = 0;
            fixtureDefinition.restitution = 1;
            var fixture = physicsWorld.createBody(bodyDefinition)
                .createFixture(fixtureDefinition);
            var id = entity.id();
            fixture.setUserData(id);
            registryEdit.addComponents(id, new Physics(fixture));
            shape.dispose();
        }
        for (var entity : circleEntities) {
            var physicsSpecification = entity.component(PhysicsSpecification.class);
            var worldPosition = entity.component(WorldPosition.class);
            var velocity = entity.component(Velocity.class);
            var circle = entity.component(Circle.class);
            var bodyDefinition = new BodyDef();
            bodyDefinition.type = physicsSpecification.bodyType();
            bodyDefinition.position.set(worldPosition.vector2());
            bodyDefinition.linearVelocity.set(velocity.vector2());
            var shape = new CircleShape();
            shape.setRadius(circle.radius());
            var fixtureDefinition = new FixtureDef();
            fixtureDefinition.shape = shape;
            fixtureDefinition.density = 1;
            fixtureDefinition.friction = 0;
            fixtureDefinition.restitution = 1;
            var fixture = physicsWorld.createBody(bodyDefinition)
                .createFixture(fixtureDefinition);
            var id = entity.id();
            fixture.setUserData(id);
            registryEdit.addComponents(id, new Physics(fixture));
            shape.dispose();
        }
    }
}
