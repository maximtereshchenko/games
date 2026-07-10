package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class TimerIncrementSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final int step;

    TimerIncrementSystem(Dominion dominion, int step) {
        super(dominion);
        this.dominion = dominion;
        this.step = step;
    }

    @Override
    void onTurnStarted() {
        if (!dominion.findCompositionsWith(AppleEaten.class).iterator().hasNext()) {
            return;
        }
        for (var timer : dominion.findCompositionsWith(Timer.class)) {
            timer.value += step;
        }
    }
}
