package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class HeadSidewaysMovementSystem extends TurnBasedSystem {

    private final Dominion dominion;

    HeadSidewaysMovementSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var results : dominion.findEntitiesWith(Head.class, Timer.class, SidewaysMovement.class, Position.class, CurrentForwardDirection.class)) {
            var headSidewaysDirection = results.comp3();
            if (results.comp2().turnsRemaining != 0) {
                continue;
            }
            results.comp4()
                .move(
                    results.comp5()
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
