package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class TagRemovalSystem extends TurnBasedSystem {

    private final Iterable<Entity> taggedEntities;
    private final Class<?>[] types;

    TagRemovalSystem(World world, Class<?>... types) {
        super(world);
        this.taggedEntities = world.entities(
            new Query().one(types)
        );
        this.types = types;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var taggedEntity : taggedEntities) {
            worldEdit.removeComponents(taggedEntity.id(), types);
        }
    }
}
