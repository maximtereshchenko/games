package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

abstract class TurnBasedSystem implements System {

    private final Iterable<Entity> turnStartedEntities;

    TurnBasedSystem(Registry registry) {
        this.turnStartedEntities = registry.view(
            new Query().all(TurnStarted.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (turnStartedEntities.iterator().hasNext()) {
            onTurnStarted(registryEdit);
        }
    }

    abstract void onTurnStarted(RegistryEdit registryEdit);
}
