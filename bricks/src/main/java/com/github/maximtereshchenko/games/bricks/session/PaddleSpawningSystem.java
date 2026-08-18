package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PaddleSpawningSystem implements System {

    private final Iterable<Entity> entities;
    private final Blueprints blueprints;

    PaddleSpawningSystem(Registry registry, Blueprints blueprints) {
        this.entities = registry.entities(
            new Query().all(Paddle.class)
        );
        this.blueprints = blueprints;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (!entities.iterator().hasNext()) {
            registryEdit.addComponents(
                registryEdit.createEntity(),
                blueprints.components(BricksBlueprints.PADDLE)
            );
        }
    }
}
