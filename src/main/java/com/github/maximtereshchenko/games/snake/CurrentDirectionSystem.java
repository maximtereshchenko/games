package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class CurrentDirectionSystem extends TurnBasedSystem {

    private final Dominion dominion;

    CurrentDirectionSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(CurrentDirection.class, NextDirection.class)) {
            result.comp1().value = result.comp2().value;
        }
    }
}
