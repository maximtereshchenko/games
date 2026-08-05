package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodGrowthIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> constantAmountFoodPolicyEntities;
    private final Iterable<Entity> foodEntities;

    FoodGrowthIncrementSystem(World world) {
        super(world);
        this.foodConsumedEntities = world.entities(
            new Query().all(FoodConsumed.class)
        );
        this.constantAmountFoodPolicyEntities = world.entities(
            new Query().all(ConstantAmountFoodPolicy.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var _ : foodConsumedEntities) {
            for (var constantAmountFoodPolicyEntity : constantAmountFoodPolicyEntities) {
                var constantAmountFoodPolicy = constantAmountFoodPolicyEntity.component(
                    ConstantAmountFoodPolicy.class
                );
                for (var foodEntity : foodEntities) {
                    var food = foodEntity.component(Food.class);
                    food.growth = Math.min(
                        food.growth + constantAmountFoodPolicy.growthStep(),
                        1.0f
                    );
                }
            }
        }
    }
}
