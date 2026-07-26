package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class EventRemovalSystem extends TurnBasedSystem {

    private final Dominion dominion;

    EventRemovalSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Event.class)) {
            dominion.deleteEntity(result.entity());
        }
    }
}
