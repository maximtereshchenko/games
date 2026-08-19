package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class LifeDecrementingSystem implements System {

    private final Iterable<Entity> entities;

    LifeDecrementingSystem(Registry registry) {
        this.entities = registry.entities(
            new Query().all(Lives.class, DecrementLivesCommand.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var livesEntity : entities) {
            var lives = livesEntity.component(Lives.class);
            lives.value--;
        }
    }
}
