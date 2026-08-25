package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class FoodWarpingSystem implements System {

    private final Iterable<Entity> foodWarpingEntities;
    private final Iterable<Entity> foodEntities;

    FoodWarpingSystem(Registry registry) {
        this.foodWarpingEntities = registry.view(
            new Query()
                .all(
                    FoodConsumed.class,
                    FoodWarping.class,
                    WorldPosition.class,
                    WorldPositionIntent.class
                )
        );
        this.foodEntities = registry.view(
            new Query().all(Food.class, WorldPosition.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
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
