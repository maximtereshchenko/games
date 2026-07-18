package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class TimerIncrementSystem extends OnFoodEatenEventSystem {

    private final Dominion dominion;
    private final int step;

    TimerIncrementSystem(Dominion dominion, int step) {
        super(dominion);
        this.dominion = dominion;
        this.step = step;
    }

    @Override
    void onFoodEaten() {
        for (var timer : dominion.findCompositionsWith(Timer.class)) {
            timer.value += step;
        }
    }
}
