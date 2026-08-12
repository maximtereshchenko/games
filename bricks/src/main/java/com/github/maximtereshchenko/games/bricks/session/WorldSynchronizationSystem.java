package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class WorldSynchronizationSystem implements System {

    private final Iterable<Entity> entities;

    WorldSynchronizationSystem(Registry registry) {
        this.entities = registry.entities(
            new Query().all(Physics.class, WorldPosition.class, Velocity.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var physics = entity.component(Physics.class);
            var worldPosition = entity.component(WorldPosition.class);
            var velocity = entity.component(Velocity.class);
            var body = physics.fixture.getBody();
            worldPosition.vector2()
                .set(
                    body.getTransform()
                        .getPosition()
                );
            velocity.vector2().set(body.getLinearVelocity());
        }
    }
}
