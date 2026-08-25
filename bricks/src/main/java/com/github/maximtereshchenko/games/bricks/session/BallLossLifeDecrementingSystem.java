package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallLossLifeDecrementingSystem implements System {

    private final Iterable<Entity> ballEntities;
    private final Iterable<Entity> livesEntities;

    BallLossLifeDecrementingSystem(Registry registry) {
        this.ballEntities = registry.view(
            new Query().all(Ball.class)
        );
        this.livesEntities = registry.view(
            new Query().all(Lives.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (!ballEntities.iterator().hasNext()) {
            for (var livesEntity : livesEntities) {
                registryEdit.addComponents(
                    livesEntity.id(),
                    DecrementLivesCommand.INSTANCE
                );
            }
        }
    }
}
