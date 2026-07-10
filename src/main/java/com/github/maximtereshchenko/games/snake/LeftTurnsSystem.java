package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class LeftTurnsSystem extends TurnBasedSystem {

    private final Dominion dominion;

    LeftTurnsSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var leftTurns : dominion.findCompositionsWith(LeftTurns.class)) {
            for (var result : dominion.findEntitiesWith(CurrentDirection.class, NextDirection.class)) {
                if (result.comp1().value.left() == result.comp2().value) {
                    leftTurns.value++;
                }
            }
        }
    }
}
