package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

final class FoodWarpingSystem implements System {

    private final Iterable<Entity> foodWarpingEntities;
    private final Iterable<Entity> foodEntities;

    FoodWarpingSystem(World world) {
        this.foodWarpingEntities = world.entities(
            new Query()
                .all(
                    FoodConsumed.class,
                    FoodWarping.class,
                    WorldPosition.class,
                    WorldPositionIntent.class
                )
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class, WorldPosition.class)
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var foodWarpingEntity : foodWarpingEntities) {
            for (var foodEntity : foodEntities) {
                var worldPosition = foodEntity.component(WorldPosition.class);
                foodWarpingEntity.component(WorldPosition.class)
                    .copy(worldPosition);
                foodWarpingEntity.component(WorldPositionIntent.class)
                    .value()
                    .copy(worldPosition);
            }
        }
    }
}
