package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SessionEndSystem extends TurnBasedSystem {

    private final Dominion dominion;

    SessionEndSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var segmentResult : dominion.findEntitiesWith(Segment.class, Position.class)) {
            for (var headResult : dominion.findEntitiesWith(Head.class, Position.class)) {
                if (segmentResult.comp2().equals(headResult.comp2())) {
                    for (var game : dominion.findCompositionsWith(Session.class)) {
                        game.status = Session.Status.ENDED;
                    }
                }
            }
        }
    }
}
