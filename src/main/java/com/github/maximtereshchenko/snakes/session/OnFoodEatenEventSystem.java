package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

abstract class OnFoodEatenEventSystem extends TurnBasedSystem {

    private final Dominion dominion;

    OnFoodEatenEventSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    final void onTurnStarted() {
        if (dominion.findCompositionsWith(FoodEaten.class).iterator().hasNext()) {
            onFoodEaten();
        }
    }

    abstract void onFoodEaten();
}
