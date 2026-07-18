package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class CurrentForwardDirectionSystem extends TurnBasedSystem {

    private final Dominion dominion;

    CurrentForwardDirectionSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(CurrentForwardDirection.class, NextForwardDirection.class)) {
            result.comp1().value = result.comp2().value;
        }
    }
}
