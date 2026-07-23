package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class AirCounterDecrementSystem extends TurnBasedSystem {

    private final Dominion dominion;

    AirCounterDecrementSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var airCounter : dominion.findCompositionsWith(AirCounter.class)) {
            airCounter.value--;
        }
    }
}
