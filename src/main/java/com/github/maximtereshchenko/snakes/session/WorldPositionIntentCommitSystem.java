package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

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
