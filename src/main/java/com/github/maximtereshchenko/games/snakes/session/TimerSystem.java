package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class TimerSystem extends TurnBasedSystem {

    private final Dominion dominion;

    TimerSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var timer : dominion.findCompositionsWith(Timer.class)) {
            if (timer.turnsLeft == 0) {
                timer.turnsLeft = timer.turns;
            }
            timer.turnsLeft--;
        }
    }
}
