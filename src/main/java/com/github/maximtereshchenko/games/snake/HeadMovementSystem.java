package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class HeadMovementSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final WorldDimensions worldDimensions;

    HeadMovementSystem(Dominion dominion, WorldDimensions worldDimensions) {
        super(dominion);
        this.dominion = dominion;
        this.worldDimensions = worldDimensions;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Position.class, CurrentDirection.class)) {
            var position = result.comp1();
            switch (result.comp2().value) {
                case UP -> position.y = adjusted(position.y + 1, worldDimensions.height());
                case DOWN -> position.y = adjusted(position.y - 1, worldDimensions.height());
                case LEFT -> position.x = adjusted(position.x - 1, worldDimensions.width());
                case RIGHT -> position.x = adjusted(position.x + 1, worldDimensions.width());
            }
        }
    }

    private int adjusted(int value, int max) {
        return (value + max) % max;
    }
}
