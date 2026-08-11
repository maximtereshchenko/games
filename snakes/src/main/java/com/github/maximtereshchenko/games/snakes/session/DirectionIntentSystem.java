package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

import java.util.Set;

final class DirectionIntentSystem extends TurnBasedSystem {

    private final Iterable<Entity> directionEntities;

    DirectionIntentSystem(Registry registry) {
        super(registry);
        this.directionEntities = registry.entities(
            new Query().all(
                Direction.class,
                DirectionIntent.class
            )
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
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
