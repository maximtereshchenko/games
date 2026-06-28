package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class InitialSegmentTimerSystem extends TurnBasedSystem {

    private final Dominion dominion;

    InitialSegmentTimerSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        if (!dominion.findCompositionsWith(AppleEaten.class).iterator().hasNext()) {
            return;
        }
        for (var initialSegmentTimer : dominion.findCompositionsWith(InitialSegmentTimer.class)) {
            initialSegmentTimer.value++;
        }
    }
}
