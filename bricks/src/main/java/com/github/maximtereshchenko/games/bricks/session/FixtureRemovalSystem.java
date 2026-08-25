package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class FixtureRemovalSystem implements System {

    private final Iterable<Entity> entities;
    private final World world;

    FixtureRemovalSystem(Registry registry, World world) {
        this.entities = registry.view(
            new Query().all(Removed.class, Fixture.class)
        );
        this.world = world;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var fixture = entity.component(Fixture.class);
            world.destroyBody(fixture.getBody());
            registryEdit.deleteEntity(entity.id());
        }
    }
}
