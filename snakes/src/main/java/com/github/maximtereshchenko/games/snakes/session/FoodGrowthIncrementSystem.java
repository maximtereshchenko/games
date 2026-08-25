package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class FoodGrowthIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> constantAmountFoodPolicyEntities;
    private final Iterable<Entity> foodEntities;

    FoodGrowthIncrementSystem(Registry registry) {
        super(registry);
        this.foodConsumedEntities = registry.view(
            new Query().all(FoodConsumed.class)
        );
        this.constantAmountFoodPolicyEntities = registry.view(
            new Query().all(ConstantAmountFoodPolicy.class)
        );
        this.foodEntities = registry.view(
            new Query().all(Food.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
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
