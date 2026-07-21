package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class HeadForwardMovementSystem extends TurnBasedSystem {

    private final Dominion dominion;

    HeadForwardMovementSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Head.class, Position.class, CurrentForwardDirection.class)) {
            var position = result.comp2();
            position.move(result.comp3().value);
        }
    }
}
