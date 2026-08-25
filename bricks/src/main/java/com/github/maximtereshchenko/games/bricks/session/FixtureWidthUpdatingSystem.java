package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class FixtureWidthUpdatingSystem implements System {

    private final Iterable<Entity> entities;
    private final World world;
    private final PhysicsObjectFactory physicsObjectFactory;

    FixtureWidthUpdatingSystem(
        Registry registry,
        World world,
        PhysicsObjectFactory physicsObjectFactory
    ) {
        this.entities = registry.view(
            new Query()
                .all(
                    UpdateWidthCommand.class,
                    Fixture.class,
                    Rectangle.class,
                    WorldPosition.class
                )
        );
        this.world = world;
        this.physicsObjectFactory = physicsObjectFactory;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var updateWidthCommand = entity.component(UpdateWidthCommand.class);
            var fixture = entity.component(Fixture.class);
            var rectangle = entity.component(Rectangle.class);
            var worldPosition = entity.component(WorldPosition.class);
            if (rectangle.halfWidth == updateWidthCommand.halfWidth()) {
                continue;
            }
            rectangle.halfWidth = updateWidthCommand.halfWidth();
            var body = fixture.getBody();
            var replacement = physicsObjectFactory.fixture(
                world,
                body.getType(),
                worldPosition,
                rectangle
            );
            replacement.setUserData(entity.id());
            registryEdit.addComponents(entity.id(), replacement);
            world.destroyBody(body);
        }
    }
}
