package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class WorldPositionIntentCommitSystem extends TurnBasedSystem {

    private final Iterable<Entity> worldPositionIntentEntities;

    WorldPositionIntentCommitSystem(Registry registry) {
        super(registry);
        this.worldPositionIntentEntities = registry.view(
            new Query().all(WorldPosition.class, WorldPositionIntent.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var entity : worldPositionIntentEntities) {
            entity.component(WorldPosition.class)
                .copy(entity.component(WorldPositionIntent.class).value());
        }
    }
}
