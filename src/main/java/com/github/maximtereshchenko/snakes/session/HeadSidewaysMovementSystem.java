package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class HeadSidewaysMovementSystem extends TurnBasedSystem {

    private final Dominion dominion;

    HeadSidewaysMovementSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Head.class, Timer.class, SidewaysMovement.class, Position.class, CurrentForwardDirection.class)) {
            var headSidewaysDirection = result.comp3();
            if (result.comp2().turnsRemaining != 0) {
                continue;
            }
            result.comp4()
                .move(
                    result.comp5()
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
