package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

abstract class TurnBasedSystem implements System {

    private final Iterable<Entity> turnStartedEntities;

    TurnBasedSystem(World world) {
        this.turnStartedEntities = world.entities(
            new Query().all(TurnStarted.class)
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        if (turnStartedEntities.iterator().hasNext()) {
            onTurnStarted(worldEdit);
        }
    }

    abstract void onTurnStarted(WorldEdit worldEdit);
}
