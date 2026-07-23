package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class AirCounterRefreshSystem extends TurnBasedSystem {

    private final Dominion dominion;

    AirCounterRefreshSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var headResult : dominion.findEntitiesWith(Head.class, Position.class)) {
            for (var airResult : dominion.findEntitiesWith(Air.class, Position.class)) {
                if (headResult.comp2().equals(airResult.comp2())) {
                    for (var airCounter : dominion.findCompositionsWith(AirCounter.class)) {
                        airCounter.value = airCounter.capacity;
                    }
                }
            }
        }
    }
}
