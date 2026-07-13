package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

import java.util.function.BiPredicate;

final class NextDirectionSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final BiPredicate<Direction, Direction> predicate;

    NextDirectionSystem(Dominion dominion, BiPredicate<Direction, Direction> predicate) {
        super(dominion);
        this.dominion = dominion;
        this.predicate = predicate;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(CurrentDirection.class, NextDirection.class)) {
            var currentDirection = result.comp1().value;
            var nextDirection = result.comp2();
            if (!predicate.test(currentDirection, nextDirection.value)) {
                nextDirection.value = currentDirection;
            }
        }
    }
}
