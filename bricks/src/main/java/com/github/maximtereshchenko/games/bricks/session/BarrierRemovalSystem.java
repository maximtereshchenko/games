package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BarrierRemovalSystem implements System {

    private final Iterable<Entity> entities;

    BarrierRemovalSystem(Registry registry) {
        this.entities = registry.entities(
            new Query().all(Barrier.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var barrier = entity.component(Barrier.class);
            barrier.remainingTimeSeconds -= deltaTimeSeconds;
            if (barrier.remainingTimeSeconds <= 0) {
                registryEdit.addComponents(entity.id(), Removed.INSTANCE);
            }
        }
    }
}
