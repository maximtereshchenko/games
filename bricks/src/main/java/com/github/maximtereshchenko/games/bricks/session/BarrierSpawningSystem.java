package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BarrierSpawningSystem implements System {

    private final Iterable<Entity> spawnBarrierEntities;
    private final Iterable<Entity> barrierEntities;
    private final Blueprints blueprints;

    BarrierSpawningSystem(Registry registry, Blueprints blueprints) {
        this.spawnBarrierEntities = registry.entities(
            new Query().all(SpawnBarrierBonus.class, Activated.class)
        );
        this.barrierEntities = registry.entities(
            new Query().all(Barrier.class)
        );
        this.blueprints = blueprints;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (
            spawnBarrierEntities.iterator().hasNext() &&
            !barrierEntities.iterator().hasNext()
        ) {
            registryEdit.addComponents(
                registryEdit.createEntity(),
                blueprints.components(BricksBlueprints.BARRIER)
            );
        }
    }
}
