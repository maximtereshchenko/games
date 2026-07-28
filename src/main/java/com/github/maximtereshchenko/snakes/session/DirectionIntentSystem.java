package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

import java.util.Set;

final class DirectionIntentSystem extends TurnBasedSystem {

    private final Iterable<Entity> directionEntities;

    DirectionIntentSystem(World world) {
        super(world);
        this.directionEntities = world.entities(
            new Query().all(
                Direction.class,
                DirectionIntent.class
            )
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : directionEntities) {
            var direction = entity.component(Direction.class);
            var directionIntent = entity.component(DirectionIntent.class);
            if (
                !isLegal(
                    direction,
                    directionIntent.value,
                    directionIntent.legalRelativeDirections
                )
            ) {
                directionIntent.value = direction;
            }
        }
    }

    private boolean isLegal(
        Direction direction,
        Direction intent,
        Set<RelativeDirection> legalRelativeDirections
    ) {
        for (var relativeDirection : legalRelativeDirections) {
            if (direction.relative(relativeDirection) == intent) {
                return true;
            }
        }
        return false;
    }
}
