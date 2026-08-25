package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class ComponentRemovalSystem implements System {

    private final Iterable<Entity> entities;
    private final Class<?>[] types;

    ComponentRemovalSystem(Registry registry, Class<?>... types) {
        this.entities = registry.view(
            new Query().one(types)
        );
        this.types = types;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            registryEdit.removeComponents(entity.id(), types);
        }
    }
}
