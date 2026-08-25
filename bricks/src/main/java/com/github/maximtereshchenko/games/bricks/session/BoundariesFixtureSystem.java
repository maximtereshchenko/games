package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BoundariesFixtureSystem implements System {

    private final Iterable<Entity> entities;
    private final PhysicsObjectFactory physicsObjectFactory;
    private final World world;

    BoundariesFixtureSystem(
        Registry registry,
        PhysicsObjectFactory physicsObjectFactory,
        World world
    ) {
        this.entities = registry.view(
            new Query().all(Boundaries.class, Fixture.class)
        );
        this.physicsObjectFactory = physicsObjectFactory;
        this.world = world;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (entities.iterator().hasNext()) {
            return;
        }
        var fixture = physicsObjectFactory.boundariesFixture(world);
        var id = registryEdit.createEntity();
        fixture.setUserData(id);
        registryEdit.addComponents(id, Boundaries.INSTANCE, fixture);
    }
}
