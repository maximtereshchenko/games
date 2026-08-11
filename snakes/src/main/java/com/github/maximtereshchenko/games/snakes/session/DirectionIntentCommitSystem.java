package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class DirectionIntentCommitSystem extends TurnBasedSystem {

    private final Iterable<Entity> directionIntentEntities;

    DirectionIntentCommitSystem(Registry registry) {
        super(registry);
        this.directionIntentEntities = registry.entities(
            new Query().all(DirectionIntent.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var entity : directionIntentEntities) {
            registryEdit.addComponents(
                entity.id(),
                entity.component(DirectionIntent.class).value
            );
        }
    }
}
