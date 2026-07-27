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
                    SidewaysMovement.class,
                    Position.class,
                    CurrentForwardDirection.class
                )
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : headEntities) {
            var sidewaysMovement = entity.component(SidewaysMovement.class);
            sidewaysMovement.remainingTurns--;
            if (sidewaysMovement.remainingTurns == 0) {
                sidewaysMovement.remainingTurns = sidewaysMovement.periodTurns;
                move(
                    entity.component(Position.class),
                    entity.component(CurrentForwardDirection.class).value,
                    sidewaysMovement
                );
            }
        }
    }

    private void move(Position position, Direction direction, SidewaysMovement sidewaysMovement) {
        position.move(direction.relative(relativeDirection(sidewaysMovement)));
        sidewaysMovement.index = (sidewaysMovement.index + 1) %
                                 sidewaysMovement.cycle;
    }

    private RelativeDirection relativeDirection(SidewaysMovement headSidewaysDirection) {
        if (headSidewaysDirection.index < headSidewaysDirection.cycle / 2) {
            return RelativeDirection.RIGHT;
        }
        return RelativeDirection.LEFT;
    }
}
