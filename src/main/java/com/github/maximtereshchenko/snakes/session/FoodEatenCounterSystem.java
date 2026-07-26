package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;

final class FoodEatenCounterSystem extends OnFoodEatenEventSystem {

    private final Iterable<Entity> foodEatenCounterEntities;

    FoodEatenCounterSystem(World world) {
        super(world);
        this.foodEatenCounterEntities = world.entities(
            new Query().all(FoodEatenCounter.class)
        );
    }

    @Override
    void onFoodEaten() {
        for (var entity : foodEatenCounterEntities) {
            entity.component(FoodEatenCounter.class).value++;
        }
    }
}
