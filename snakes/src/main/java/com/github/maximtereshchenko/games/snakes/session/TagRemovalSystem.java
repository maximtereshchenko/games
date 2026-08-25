package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class TagRemovalSystem implements System {

    private final Iterable<Entity> taggedEntities;
    private final Class<?>[] types;

    TagRemovalSystem(Registry registry, Class<?>... types) {
        this.taggedEntities = registry.view(
            new Query().one(types)
        );
        this.types = types;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var taggedEntity : taggedEntities) {
            registryEdit.removeComponents(taggedEntity.id(), types);
        }
    }
}
