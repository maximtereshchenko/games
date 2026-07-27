package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class PlannedMovementSystem extends TurnBasedSystem {

    private final Iterable<Entity> movementEntities;

    PlannedMovementSystem(World world) {
        super(world);
        this.movementEntities = world.entities(
            new Query().all(ForwardMovement.class, PlannedMovement.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : movementEntities) {
            var forwardMovement = entity.component(ForwardMovement.class);
            var plannedMovement = entity.component(PlannedMovement.class);
            if (isLegal(forwardMovement.direction, plannedMovement)) {
                forwardMovement.direction = plannedMovement.direction;
            } else {
                plannedMovement.direction = forwardMovement.direction;
            }
        }
    }

    private boolean isLegal(
        Direction direction,
        PlannedMovement plannedMovement
    ) {
        for (var relativeDirection : plannedMovement.legalRelativeDirections) {
            if (direction.relative(relativeDirection) == plannedMovement.direction) {
                return true;
            }
        }
        return false;
    }
}
