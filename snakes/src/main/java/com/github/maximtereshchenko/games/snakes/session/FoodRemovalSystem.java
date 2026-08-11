package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class FoodRemovalSystem implements System {

    private final Iterable<Entity> foodWarpingEntities;
    private final Iterable<Entity> foodEntities;

    FoodRemovalSystem(World world) {
        this.foodWarpingEntities = world.entities(
            new Query()
                .all(FoodConsumed.class, FoodWarping.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var _ : foodWarpingEntities) {
            for (var foodEntity : foodEntities) {
                worldEdit.deleteEntity(foodEntity.id());
            }
        }
    }
}
