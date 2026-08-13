package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BarrierTimeExtendingSystem implements System {

    private final Iterable<Entity> spawnBarrierEntities;
    private final Iterable<Entity> barrierEntities;

    BarrierTimeExtendingSystem(Registry registry) {
        this.spawnBarrierEntities = registry.entities(
            new Query().all(SpawnBarrier.class, Removed.class)
        );
        this.barrierEntities = registry.entities(
            new Query().all(Barrier.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var spawnBarrierEntity : spawnBarrierEntities) {
            var spawnBarrier = spawnBarrierEntity.component(SpawnBarrier.class);
            for (var barrierEntity : barrierEntities) {
                var barrier = barrierEntity.component(Barrier.class);
                barrier.remainingTimeSeconds += spawnBarrier.extraTimeSeconds();
            }
        }
    }
}
