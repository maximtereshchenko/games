package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class HeadCollisionTargetRemovalSystem extends TurnBasedSystem {

    private final Iterable<Entity> headCollisionTargetEntities;

    HeadCollisionTargetRemovalSystem(World world) {
        super(world);
        this.headCollisionTargetEntities = world.entities(
            new Query().all(HeadCollisionTarget.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : headCollisionTargetEntities) {
            worldEdit.removeComponents(entity.id(), HeadCollisionTarget.class);
        }
    }
}
