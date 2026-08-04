package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodGrowthIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> foodPolicyEntities;
    private final Iterable<Entity> foodEntities;

    FoodGrowthIncrementSystem(World world) {
        super(world);
        this.foodConsumedEntities = world.entities(
            new Query().all(FoodConsumed.class)
        );
        this.foodPolicyEntities = world.entities(
            new Query().all(FoodPolicy.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var _ : foodConsumedEntities) {
            for (var foodPolicyEntity : foodPolicyEntities) {
                var foodPolicy = foodPolicyEntity.component(FoodPolicy.class);
                for (var foodEntity : foodEntities) {
                    var food = foodEntity.component(Food.class);
                    food.growth = Math.min(
                        food.growth + foodPolicy.growthStep(),
                        1.0f
                    );
                }
            }
        }
    }
}
