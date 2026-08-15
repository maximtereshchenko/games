package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class LifeIncrementingSystem implements System {

    private final Iterable<Entity> incrementLivesEntities;
    private final Iterable<Entity> livesEntities;

    LifeIncrementingSystem(Registry registry) {
        this.incrementLivesEntities = registry.entities(
            new Query().all(IncrementLives.class, Activated.class)
        );
        this.livesEntities = registry.entities(
            new Query().all(Lives.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var _ : incrementLivesEntities) {
            for (var livesEntity : livesEntities) {
                var lives = livesEntity.component(Lives.class);
                lives.value++;
            }
        }
    }
}
