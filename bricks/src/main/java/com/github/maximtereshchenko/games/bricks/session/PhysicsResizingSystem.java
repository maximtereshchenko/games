package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;
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
                    Fixture.class,
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
            var fixture = entity.component(Fixture.class);
            var rectangle = entity.component(Rectangle.class);
            var worldPosition = entity.component(WorldPosition.class);
            var body = fixture.getBody();
            var shape = shape(rectangle);
            var replacement = fixtureFactory.fixture(
                world,
                body.getType(),
                worldPosition.vector2(),
                shape,
                false,
                fixture.getFilterData().groupIndex
            );
            replacement.setUserData(entity.id());
            registryEdit.addComponents(entity.id(), replacement);
            world.destroyBody(body);
            shape.dispose();
        }
    }

    //TODO
    private Shape shape(Rectangle rectangle) {
        var shape = new PolygonShape();
        shape.setAsBox(rectangle.halfWidth, rectangle.halfHeight);
        return shape;
    }
}
