package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import com.github.maximtereshchenko.games.ecs.WorldEdit;

final class WorldPositionIntentCommitSystem extends TurnBasedSystem {

    private final Iterable<Entity> worldPositionIntentEntities;

    WorldPositionIntentCommitSystem(World world) {
        super(world);
        this.worldPositionIntentEntities = world.entities(
            new Query().all(WorldPosition.class, WorldPositionIntent.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : worldPositionIntentEntities) {
            entity.component(WorldPosition.class)
                .copy(entity.component(WorldPositionIntent.class).value());
        }
    }
}
