package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PhysicsSynchronizationSystem implements System {

    private final Iterable<Entity> entities;

    PhysicsSynchronizationSystem(Registry registry) {
        this.entities = registry.entities(
            new Query().all(Fixture.class, Velocity.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var fixture = entity.component(Fixture.class);
            var velocity = entity.component(Velocity.class);
            fixture.getBody()
                .setLinearVelocity(velocity.vector2());
        }
    }
}
