package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class HeadMovementSystem extends TurnBasedSystem {

    private final Dominion dominion;

    HeadMovementSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var worldDimensions : dominion.findCompositionsWith(WorldDimensions.class)) {
            for (var result : dominion.findEntitiesWith(Head.class, Position.class, CurrentDirection.class)) {
                var position = result.comp2();
                switch (result.comp3().value) {
                    case UP -> position.y = adjusted(position.y + 1, worldDimensions.height());
                    case DOWN -> position.y = adjusted(position.y - 1, worldDimensions.height());
                    case LEFT -> position.x = adjusted(position.x - 1, worldDimensions.width());
                    case RIGHT -> position.x = adjusted(position.x + 1, worldDimensions.width());
                }
            }
        }
    }

    private int adjusted(int value, int max) {
        return (value + max) % max;
    }
}
