package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.*;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PhysicsRegistrationSystem implements System {

    private final Iterable<Entity> entities;
    private final World world;
    private final FixtureFactory fixtureFactory;

    PhysicsRegistrationSystem(
        Registry registry,
        World world,
        FixtureFactory fixtureFactory
    ) {
        this.entities = registry.entities(
            new Query()
                .all(BodyDef.BodyType.class, WorldPosition.class)
                .one(Rectangle.class, Circle.class)
        );
        this.world = world;
        this.fixtureFactory = fixtureFactory;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var bodyType = entity.component(BodyDef.BodyType.class);
            var worldPosition = entity.component(WorldPosition.class);
            var id = entity.id();
            var shape = shape(entity);
            var fixture = fixtureFactory.fixture(
                world,
                bodyType,
                worldPosition.vector2(),
                shape
            );
            fixture.setUserData(id);
            registryEdit.addComponents(id, new Physics(fixture));
            shape.dispose();
        }
    }

    private Shape shape(Entity entity) {
        var rectangle = entity.component(Rectangle.class);
        if (rectangle == null) {
            var circle = entity.component(Circle.class);
            var shape = new CircleShape();
            shape.setRadius(circle.radius());
            return shape;
        }
        var shape = new PolygonShape();
        shape.setAsBox(rectangle.width / 2, rectangle.height / 2);
        return shape;
    }
}
