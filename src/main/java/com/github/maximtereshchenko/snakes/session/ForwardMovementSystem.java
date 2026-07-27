package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class ForwardMovementSystem extends TurnBasedSystem {

    private final Iterable<Entity> movementEntities;

    ForwardMovementSystem(World world) {
        super(world);
        this.movementEntities = world.entities(
            new Query().all(Position.class, ForwardMovement.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : movementEntities) {
            var forwardMovement = entity.component(ForwardMovement.class);
            forwardMovement.remainingTurns--;
            if (forwardMovement.remainingTurns == 0) {
                forwardMovement.remainingTurns = forwardMovement.periodTurns;
                entity.component(Position.class).move(forwardMovement.direction);
            }
        }
    }
}
