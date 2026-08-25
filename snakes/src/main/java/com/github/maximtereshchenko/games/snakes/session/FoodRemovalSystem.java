package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class FoodRemovalSystem implements System {

    private final Iterable<Entity> foodWarpingEntities;
    private final Iterable<Entity> foodEntities;

    FoodRemovalSystem(Registry registry) {
        this.foodWarpingEntities = registry.view(
            new Query()
                .all(FoodConsumed.class, FoodWarping.class)
        );
        this.foodEntities = registry.view(
            new Query().all(Food.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var _ : foodWarpingEntities) {
            for (var foodEntity : foodEntities) {
                registryEdit.deleteEntity(foodEntity.id());
            }
        }
    }
}
