package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;

final class NextForwardDirectionSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final Mode mode;

    NextForwardDirectionSystem(Dominion dominion, Mode mode) {
        super(dominion);
        this.dominion = dominion;
        this.mode = mode;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(CurrentForwardDirection.class, NextForwardDirection.class)) {
            var currentDirection = result.comp1().value;
            var nextDirection = result.comp2();
            if (!isLegal(currentDirection, nextDirection.value)) {
                nextDirection.value = currentDirection;
            }
        }
    }

    private boolean isLegal(Direction current, Direction next) {
        for (var legalTurnDirection : mode.legalTurnDirections()) {
            if (current.relative(legalTurnDirection) == next) {
                return true;
            }
        }
        return false;
    }
}
