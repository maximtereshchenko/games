package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class NextDirectionSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final Mode mode;

    NextDirectionSystem(Dominion dominion, Mode mode) {
        super(dominion);
        this.dominion = dominion;
        this.mode = mode;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(CurrentDirection.class, NextDirection.class)) {
            var currentDirection = result.comp1().value;
            var nextDirection = result.comp2();
            for (var legalTurn : mode.legalTurns()) {
                if (currentDirection.after(legalTurn) == nextDirection.value) {
                    return;
                }
            }
            nextDirection.value = currentDirection;
        }
    }
}
