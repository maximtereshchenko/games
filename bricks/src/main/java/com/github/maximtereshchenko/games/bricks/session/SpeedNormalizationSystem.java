package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class SpeedNormalizationSystem implements System {

    private final Iterable<Entity> entities;

    SpeedNormalizationSystem(Registry registry) {
        this.entities = registry.view(
            new Query().all(Speed.class, Velocity.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var speed = entity.component(Speed.class);
            var velocity = entity.component(Velocity.class);
            velocity.vector2().nor().scl(speed.value());
        }
    }
}
