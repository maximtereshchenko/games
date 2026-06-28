package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

import java.awt.Point;
import java.util.HashSet;

final class GameEndSystem extends TurnBasedSystem {

    private final Dominion dominion;

    GameEndSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        var points = new HashSet<Point>();
        for (var point : dominion.findCompositionsWith(Point.class)) {
            if (!points.add(point)) {
                for (var game : dominion.findCompositionsWith(Game.class)) {
                    game.status = Game.Status.ENDED;
                }
                return;
            }
        }
    }
}
