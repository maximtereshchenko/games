package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class DirectedMovementSystem extends TurnBasedSystem {

    private final Iterable<Entity> movingEntities;

    DirectedMovementSystem(Registry registry) {
        super(registry);
        this.movingEntities = registry.entities(
            new Query().all(
                DirectedMovement.class,
                Direction.class,
                WorldPositionIntent.class
            )
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var entity : movingEntities) {
            var directedMovement = entity.component(DirectedMovement.class);
            directedMovement.remainingTurns--;
            if (directedMovement.remainingTurns == 0) {
                directedMovement.remainingTurns = directedMovement.periodTurns;
                entity.component(WorldPositionIntent.class)
                    .value()
                    .move(entity.component(Direction.class));
            }
        }
    }
}
