package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class FoodEatenCounterSystem extends OnFoodEatenEventSystem {

    private final Dominion dominion;

    FoodEatenCounterSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onFoodEaten() {
        for (var foodEatenCounter : dominion.findCompositionsWith(FoodEatenCounter.class)) {
            foodEatenCounter.value++;
        }
    }
}
