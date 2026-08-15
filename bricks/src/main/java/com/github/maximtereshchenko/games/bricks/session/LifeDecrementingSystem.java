package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class LifeDecrementingSystem implements System {

    private final Iterable<Entity> ballEntities;
    private final Iterable<Entity> livesEntities;

    LifeDecrementingSystem(Registry registry) {
        this.ballEntities = registry.entities(
            new Query().all(Ball.class)
        );
        this.livesEntities = registry.entities(
            new Query().all(Lives.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (ballEntities.iterator().hasNext()) {
            return;
        }
        for (var livesEntity : livesEntities) {
            var lives = livesEntity.component(Lives.class);
            lives.value--;
        }
    }
}
