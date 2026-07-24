package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class FoodEatingSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final EntityFactory entityFactory;

    FoodEatingSystem(Dominion dominion, EntityFactory entityFactory) {
        super(dominion);
        this.dominion = dominion;
        this.entityFactory = entityFactory;
    }

    @Override
    void onTurnStarted() {
        for (var foodResult : dominion.findEntitiesWith(Food.class, HeadCollisionTarget.class)) {
            dominion.deleteEntity(foodResult.entity());
            entityFactory.createFoodEatenEvent(dominion);
        }
    }
}
