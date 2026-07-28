package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class PositionIntentCommitSystem extends TurnBasedSystem {

    private final Iterable<Entity> positionIntentEntities;

    PositionIntentCommitSystem(World world) {
        super(world);
        this.positionIntentEntities = world.entities(
            new Query().all(WorldPosition.class, WorldPositionIntent.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : positionIntentEntities) {
            entity.component(WorldPosition.class)
                .copy(entity.component(WorldPositionIntent.class).value);
        }
    }
}
