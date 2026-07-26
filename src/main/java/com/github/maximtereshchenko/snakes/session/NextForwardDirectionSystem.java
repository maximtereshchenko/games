package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

import java.util.Set;

final class NextForwardDirectionSystem extends TurnBasedSystem {

    private final Dominion dominion;

    NextForwardDirectionSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(CurrentForwardDirection.class, NextForwardDirection.class, LegalRelativeDirections.class)) {
            var currentDirection = result.comp1().value;
            var nextDirection = result.comp2();
            if (!isLegal(currentDirection, nextDirection.value, result.comp3().value())) {
                nextDirection.value = currentDirection;
            }
        }
    }

    private boolean isLegal(
        Direction current,
        Direction next,
        Set<RelativeDirection> relativeDirections
    ) {
        for (var relativeDirection : relativeDirections) {
            if (current.relative(relativeDirection) == next) {
                return true;
            }
        }
        return false;
    }
}
