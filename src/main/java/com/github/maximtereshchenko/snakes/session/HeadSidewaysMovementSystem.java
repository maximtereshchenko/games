package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class HeadSidewaysMovementSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;

    HeadSidewaysMovementSystem(World world) {
        super(world);
        this.headEntities = world.entities(
            new Query()
                .all(
                    Head.class,
                    Timer.class,
                    SidewaysMovement.class,
                    Position.class,
                    CurrentForwardDirection.class
                )
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var head : headEntities) {
            var headSidewaysDirection = head.component(SidewaysMovement.class);
            if (head.component(Timer.class).turnsRemaining != 0) {
                continue;
            }
            head.component(Position.class)
                .move(
                    head.component(CurrentForwardDirection.class)
                        .value
                        .relative(relativeDirection(headSidewaysDirection))
                );
            headSidewaysDirection.index = (headSidewaysDirection.index + 1) %
                                          headSidewaysDirection.cycle;
        }
    }

    private RelativeDirection relativeDirection(SidewaysMovement headSidewaysDirection) {
        if (headSidewaysDirection.index < headSidewaysDirection.cycle / 2) {
            return RelativeDirection.RIGHT;
        }
        return RelativeDirection.LEFT;
    }
}
