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
        for (var results : dominion.findEntitiesWith(Timer.class, SidewaysDirection.class, Position.class, CurrentForwardDirection.class)) {
            var headSidewaysDirection = results.comp2();
            if (headSidewaysDirection.cycle == 0 || results.comp1().turnsLeft != 0) {
                continue;
            }
            for (var worldDimensions : dominion.findCompositionsWith(WorldDimensions.class)) {
                results.comp3()
                    .move(
                        worldDimensions,
                        results.comp4()
                            .value
                            .relative(relativeDirection(headSidewaysDirection))
                    );
            }
            headSidewaysDirection.index = (headSidewaysDirection.index + 1) %
                                          headSidewaysDirection.cycle;
        }
    }

    private RelativeDirection relativeDirection(SidewaysDirection headSidewaysDirection) {
        if (headSidewaysDirection.index < headSidewaysDirection.cycle / 2) {
            return RelativeDirection.RIGHT;
        }
        return RelativeDirection.LEFT;
    }
}
