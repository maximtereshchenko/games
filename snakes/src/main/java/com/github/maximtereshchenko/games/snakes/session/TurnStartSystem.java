package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class TurnStartSystem implements System {

    private final Iterable<Entity> turnTimerEntities;

    TurnStartSystem(Registry registry) {
        this.turnTimerEntities = registry.entities(
            new Query().all(TurnTimer.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : turnTimerEntities) {
            var turnTimer = entity.component(TurnTimer.class);
            turnTimer.timePassedSeconds += deltaTimeSeconds;
            if (turnTimer.timePassedSeconds > turnTimer.turnLengthSeconds) {
                turnTimer.timePassedSeconds -= turnTimer.turnLengthSeconds;
                registryEdit.addComponents(entity.id(), TurnStarted.INSTANCE);
            }
        }
    }
}
