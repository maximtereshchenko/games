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
        for (var segmentResults : dominion.findCompositionsWith(Segment.class, Position.class)) {
            for (var headResults : dominion.findCompositionsWith(Head.class, Position.class)) {
                if (segmentResults.comp2().equals(headResults.comp2())) {
                    for (var game : dominion.findCompositionsWith(Session.class)) {
                        game.status = Session.Status.ENDED;
                    }
                }
            }
        }
    }
}
