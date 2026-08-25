package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BarrierTimeExtendingSystem implements System {

    private final Iterable<Entity> spawnBarrierEntities;
    private final Iterable<Entity> barrierEntities;

    BarrierTimeExtendingSystem(Registry registry) {
        this.spawnBarrierEntities = registry.view(
            new Query().all(SpawnBarrierBonus.class, Activated.class)
        );
        this.barrierEntities = registry.view(
            new Query().all(Barrier.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var spawnBarrierEntity : spawnBarrierEntities) {
            var spawnBarrier = spawnBarrierEntity.component(SpawnBarrierBonus.class);
            for (var barrierEntity : barrierEntities) {
                var barrier = barrierEntity.component(Barrier.class);
                barrier.remainingTimeSeconds += spawnBarrier.extraTimeSeconds();
            }
        }
    }
}
