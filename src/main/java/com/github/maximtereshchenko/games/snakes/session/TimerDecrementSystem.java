package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class TimerDecrementSystem extends TurnBasedSystem {

    private final Dominion dominion;

    TimerDecrementSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var timer : dominion.findCompositionsWith(Timer.class)) {
            timer.value--;
        }
    }
}
