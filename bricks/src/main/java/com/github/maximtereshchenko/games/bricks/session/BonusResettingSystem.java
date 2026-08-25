package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BonusResettingSystem implements System {

    private final Iterable<Entity> ballEntities;
    private final Iterable<Entity> bonusEntities;

    BonusResettingSystem(Registry registry) {
        this.ballEntities = registry.view(
            new Query().all(Ball.class)
        );
        this.bonusEntities = registry.view(
            new Query().all(Bonus.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (ballEntities.iterator().hasNext()) {
            return;
        }
        for (var bonusEntity : bonusEntities) {
            registryEdit.addComponents(
                bonusEntity.id(),
                Removed.INSTANCE
            );
        }
    }
}
