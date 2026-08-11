package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import com.github.maximtereshchenko.games.ecs.WorldEdit;

final class SidewaysMovementSystem extends TurnBasedSystem {

    private final Iterable<Entity> sidewaysMovementEntities;

    SidewaysMovementSystem(World world) {
        super(world);
        this.sidewaysMovementEntities = world.entities(
            new Query()
                .all(
                    SidewaysMovement.class,
                    Direction.class,
                    WorldPositionIntent.class
                )
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : sidewaysMovementEntities) {
            var sidewaysMovement = entity.component(SidewaysMovement.class);
            sidewaysMovement.remainingTurns--;
            if (sidewaysMovement.remainingTurns == 0) {
                sidewaysMovement.remainingTurns = sidewaysMovement.periodTurns;
                entity.component(WorldPositionIntent.class)
                    .value()
                    .move(
                        entity.component(Direction.class)
                            .relative(relativeDirection(sidewaysMovement))
                    );
                sidewaysMovement.index = (sidewaysMovement.index + 1) %
                                         sidewaysMovement.cycle;
            }
        }
    }

    private RelativeDirection relativeDirection(SidewaysMovement headSidewaysDirection) {
        if (headSidewaysDirection.index < headSidewaysDirection.cycle / 2) {
            return RelativeDirection.RIGHT;
        }
        return RelativeDirection.LEFT;
    }
}
