package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class DirectionIntentCommitSystem extends TurnBasedSystem {

    private final Iterable<Entity> directionIntentEntities;

    DirectionIntentCommitSystem(World world) {
        super(world);
        this.directionIntentEntities = world.entities(
            new Query().all(DirectionIntent.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : directionIntentEntities) {
            worldEdit.addComponents(
                entity.id(),
                entity.component(DirectionIntent.class).value
            );
        }
    }
}
