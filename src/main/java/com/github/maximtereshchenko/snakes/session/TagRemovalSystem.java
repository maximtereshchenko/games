package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

final class TagRemovalSystem implements System {

    private final Iterable<Entity> taggedEntities;
    private final Class<?>[] types;

    TagRemovalSystem(World world, Class<?>... types) {
        this.taggedEntities = world.entities(
            new Query().one(types)
        );
        this.types = types;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var taggedEntity : taggedEntities) {
            worldEdit.removeComponents(taggedEntity.id(), types);
        }
    }
}
