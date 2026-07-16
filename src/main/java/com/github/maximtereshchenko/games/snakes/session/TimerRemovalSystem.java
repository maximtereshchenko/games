package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class TimerRemovalSystem extends TurnBasedSystem {

    private final Dominion dominion;

    TimerRemovalSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Timer.class)) {
            if (result.comp().value == 0) {
                dominion.deleteEntity(result.entity());
            }
        }
    }
}
