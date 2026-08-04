package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodOpacitySynchronisationSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodEntities;

    FoodOpacitySynchronisationSystem(World world) {
        super(world);
        this.foodEntities = world.entities(
            new Query().all(Food.class, Opacity.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var foodEntity : foodEntities) {
            foodEntity.component(Opacity.class).value =
                foodEntity.component(Food.class).growth;
        }
    }
}
