package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class DecrementLivesBonusSystem implements System {

    private final Iterable<Entity> decrementLivesEntities;
    private final Iterable<Entity> livesEntities;

    DecrementLivesBonusSystem(Registry registry) {
        this.decrementLivesEntities = registry.view(
            new Query().all(DecrementLivesBonus.class, Activated.class)
        );
        this.livesEntities = registry.view(
            new Query().all(Lives.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var _ : decrementLivesEntities) {
            for (var livesEntity : livesEntities) {
                registryEdit.addComponents(
                    livesEntity.id(),
                    DecrementLivesCommand.INSTANCE
                );
            }
        }
    }
}
