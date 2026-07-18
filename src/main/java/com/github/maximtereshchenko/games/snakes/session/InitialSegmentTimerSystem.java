package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class InitialSegmentTimerSystem extends OnFoodEatenEventSystem {

    private final Dominion dominion;
    private final int step;

    InitialSegmentTimerSystem(Dominion dominion, int step) {
        super(dominion);
        this.dominion = dominion;
        this.step = step;
    }

    @Override
    void onFoodEaten() {
        for (var initialSegmentTimer : dominion.findCompositionsWith(InitialSegmentTimer.class)) {
            initialSegmentTimer.value += step;
        }
    }
}
