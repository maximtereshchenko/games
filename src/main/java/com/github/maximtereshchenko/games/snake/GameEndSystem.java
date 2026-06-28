package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

import java.util.HashSet;

final class GameEndSystem extends TurnBasedSystem {

    private final Dominion dominion;

    GameEndSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        var positions = new HashSet<Position>();
        for (var position : dominion.findCompositionsWith(Position.class)) {
            if (!positions.add(position)) {
                for (var game : dominion.findCompositionsWith(Game.class)) {
                    game.status = Game.Status.ENDED;
                }
                return;
            }
        }
    }
}
