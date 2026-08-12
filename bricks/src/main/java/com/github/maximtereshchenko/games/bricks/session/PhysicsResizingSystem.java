package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PhysicsResizingSystem implements System {

    private final Iterable<Entity> entities;
    private final World world;
    private final FixtureFactory fixtureFactory;

    PhysicsResizingSystem(
        Registry registry,
        World world,
        FixtureFactory fixtureFactory
    ) {
        this.entities = registry.entities(
            new Query()
                .all(
                    Resized.class,
                    Physics.class,
                    Rectangle.class,
                    WorldPosition.class
                )
        );
        this.world = world;
        this.fixtureFactory = fixtureFactory;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var physics = entity.component(Physics.class);
            var rectangle = entity.component(Rectangle.class);
            var worldPosition = entity.component(WorldPosition.class);
            var shape = shape(rectangle);
            var fixture = fixtureFactory.fixture(
                world,
                physics.fixture.getBody().getType(),
                worldPosition.vector2(),
                shape,
                false
            );
            fixture.setUserData(entity.id());
            physics.fixture = fixture;
            shape.dispose();
        }
    }

    private Shape shape(Rectangle rectangle) {
        var shape = new PolygonShape();
        shape.setAsBox(rectangle.width / 2, rectangle.height / 2);
        return shape;
    }
}
