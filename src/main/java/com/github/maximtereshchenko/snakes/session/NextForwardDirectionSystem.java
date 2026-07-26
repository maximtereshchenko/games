package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

import java.util.Set;

final class NextForwardDirectionSystem extends TurnBasedSystem {

    private final Iterable<Entity> directionEntities;

    NextForwardDirectionSystem(World world) {
        super(world);
        this.directionEntities = world.entities(
            new Query()
                .all(
                    CurrentForwardDirection.class,
                    NextForwardDirection.class,
                    LegalRelativeDirections.class
                )
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : directionEntities) {
            var currentDirection = entity.component(CurrentForwardDirection.class).value;
            var nextDirection = entity.component(NextForwardDirection.class);
            if (
                !isLegal(
                    currentDirection,
                    nextDirection.value,
                    entity.component(LegalRelativeDirections.class).value()
                )
            ) {
                nextDirection.value = currentDirection;
            }
        }
    }

    private boolean isLegal(
        Direction current,
        Direction next,
        Set<RelativeDirection> relativeDirections
    ) {
        for (var relativeDirection : relativeDirections) {
            if (current.relative(relativeDirection) == next) {
                return true;
            }
        }
        return false;
    }
}
