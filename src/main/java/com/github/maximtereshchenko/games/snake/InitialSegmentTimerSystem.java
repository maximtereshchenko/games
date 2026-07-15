package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class InitialSegmentTimerSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final int step;

    InitialSegmentTimerSystem(Dominion dominion, int step) {
        super(dominion);
        this.dominion = dominion;
        this.step = step;
    }

    @Override
    void onTurnStarted() {
        if (!dominion.findCompositionsWith(FoodEaten.class).iterator().hasNext()) {
            return;
        }
        for (var initialSegmentTimer : dominion.findCompositionsWith(InitialSegmentTimer.class)) {
            initialSegmentTimer.value += step;
        }
    }
}
