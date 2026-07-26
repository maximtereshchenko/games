package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SessionEndSystem extends TurnBasedSystem {

    private final Dominion dominion;

    SessionEndSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        if (dominion.findEntitiesWith(Segment.class, HeadCollisionTarget.class).iterator().hasNext())
            for (var game : dominion.findCompositionsWith(Session.class)) {
                game.status = Session.Status.ENDED;
            }
    }
}
