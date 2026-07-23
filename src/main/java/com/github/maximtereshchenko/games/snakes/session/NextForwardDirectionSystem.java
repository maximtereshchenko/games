package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class NextForwardDirectionSystem extends TurnBasedSystem {

    private final Dominion dominion;

    NextForwardDirectionSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(CurrentForwardDirection.class, NextForwardDirection.class, LegalRelativeDirection.class)) {
            var currentDirection = result.comp1().value;
            var nextDirection = result.comp2();
            if (currentDirection.relative(result.comp3().value()) != nextDirection.value) {
                nextDirection.value = currentDirection;
            }
        }
    }
}
