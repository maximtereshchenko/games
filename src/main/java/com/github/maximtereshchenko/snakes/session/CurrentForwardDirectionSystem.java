package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class CurrentForwardDirectionSystem extends TurnBasedSystem {

    private final Iterable<Entity> directionEntities;

    CurrentForwardDirectionSystem(World world) {
        super(world);
        this.directionEntities = world.entities(
            new Query().all(CurrentForwardDirection.class, NextForwardDirection.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : directionEntities) {
            entity.component(CurrentForwardDirection.class).value =
                entity.component(NextForwardDirection.class).value;
        }
    }
}
