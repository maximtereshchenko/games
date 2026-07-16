package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class FoodEatenCounterSystem extends TurnBasedSystem {

    private final Dominion dominion;

    FoodEatenCounterSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        if (dominion.findCompositionsWith(FoodEaten.class).iterator().hasNext()) {
            for (var foodEatenCounter : dominion.findCompositionsWith(FoodEatenCounter.class)) {
                foodEatenCounter.value++;
            }
        }
    }
}
