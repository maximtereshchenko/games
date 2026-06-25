package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class EventRemovalSystem implements Runnable {

    private final Dominion dominion;

    EventRemovalSystem(Dominion dominion) {
        this.dominion = dominion;
    }

    @Override
    public void run() {
        for (var result : dominion.findEntitiesWith(Event.class)) {
            dominion.deleteEntity(result.entity());
        }
    }
}
