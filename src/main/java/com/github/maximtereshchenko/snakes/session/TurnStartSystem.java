package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

final class TurnStartSystem implements System {

    private final Iterable<Entity> turnTimerEntities;

    TurnStartSystem(World world) {
        this.turnTimerEntities = world.entities(
            new Query().all(TurnTimer.class)
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var entity : turnTimerEntities) {
            var turnTimer = entity.component(TurnTimer.class);
            turnTimer.timePassedSeconds += deltaTimeSeconds;
            if (turnTimer.timePassedSeconds > turnTimer.turnLengthSeconds) {
                turnTimer.timePassedSeconds -= turnTimer.turnLengthSeconds;
                worldEdit.addComponents(entity.id(), TurnStarted.INSTANCE);
            }
        }
    }
}
