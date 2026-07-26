package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class HeadForwardMovementSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;

    HeadForwardMovementSystem(World world) {
        super(world);
        this.headEntities = world.entities(
            new Query().all(Head.class, Position.class, CurrentForwardDirection.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var head : headEntities) {
            head.component(Position.class)
                .move(head.component(CurrentForwardDirection.class).value);
        }
    }
}
