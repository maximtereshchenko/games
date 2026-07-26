package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SessionStatisticsSystem extends TurnBasedSystem {

    private final Dominion dominion;

    SessionStatisticsSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var accumulator : dominion.findCompositionsWith(SessionStatisticsAccumulator.class)) {
            var sessionStatistics = accumulator.value;
            for (var result : dominion.findEntitiesWith(CurrentForwardDirection.class, NextForwardDirection.class)) {
                if (result.comp1().value.left() == result.comp2().value) {
                    sessionStatistics.put(
                        SessionStatistics.LEFT_TURNS,
                        sessionStatistics.get(SessionStatistics.LEFT_TURNS) + 1
                    );
                }
            }
        }
    }
}
