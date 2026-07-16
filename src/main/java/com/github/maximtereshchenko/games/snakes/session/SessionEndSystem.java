package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

import java.util.HashSet;

final class SessionEndSystem extends TurnBasedSystem {

    private final Dominion dominion;

    SessionEndSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        var positions = new HashSet<Position>();
        for (var position : dominion.findCompositionsWith(Position.class)) {
            if (!positions.add(position)) {
                for (var game : dominion.findCompositionsWith(Session.class)) {
                    game.status = Session.Status.ENDED;
                }
                return;
            }
        }
    }
}
