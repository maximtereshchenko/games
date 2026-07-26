package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

abstract class OnFoodEatenEventSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodEatenEntities;

    OnFoodEatenEventSystem(World world) {
        super(world);
        this.foodEatenEntities = world.entities(
            new Query().all(FoodEaten.class)
        );
    }

    @Override
    final void onTurnStarted(WorldEdit worldEdit) {
        if (foodEatenEntities.iterator().hasNext()) {
            onFoodEaten();
        }
    }

    abstract void onFoodEaten();
}
