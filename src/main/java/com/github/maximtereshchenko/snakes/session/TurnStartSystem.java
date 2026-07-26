package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

final class TurnStartSystem implements System {

    private final Iterable<Entity> turnTimerEntities;
    private final EntityFactory entityFactory;

    TurnStartSystem(
        World world,
        EntityFactory entityFactory
    ) {
        this.turnTimerEntities = world.entities(
            new Query().all(TurnTimer.class)
        );
        this.entityFactory = entityFactory;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var entity : turnTimerEntities) {
            var turnTimer = entity.component(TurnTimer.class);
            turnTimer.timePassedSeconds += deltaTimeSeconds;
            if (turnTimer.timePassedSeconds > turnTimer.turnLengthSeconds) {
                turnTimer.timePassedSeconds -= turnTimer.turnLengthSeconds;
                entityFactory.createTurnStartedEvent(worldEdit);
            }
        }
    }
}
