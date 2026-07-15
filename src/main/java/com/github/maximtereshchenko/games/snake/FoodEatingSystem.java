package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class FoodEatingSystem extends TurnBasedSystem {

    private final Dominion dominion;

    FoodEatingSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var foodResult : dominion.findEntitiesWith(Food.class, Position.class)) {
            for (var headResult : dominion.findEntitiesWith(Head.class, Position.class)) {
                if (headResult.comp2().equals(foodResult.comp2())) {
                    dominion.deleteEntity(foodResult.entity());
                    dominion.createEntity(FoodEaten.INSTANCE, Event.INSTANCE);
                }
            }
        }
    }
}
