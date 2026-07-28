package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodConsumedIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> statisticsEntities;

    FoodConsumedIncrementSystem(World world) {
        super(world);
        this.statisticsEntities = world.entities(
            new Query().all(FoodConsumed.class, Statistics.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var statisticsEntity : statisticsEntities) {
            var sessionStatistics = statisticsEntity.component(Statistics.class).value;
            sessionStatistics.put(
                SessionMetric.FOOD_CONSUMED,
                sessionStatistics.get(SessionMetric.FOOD_CONSUMED) + 1
            );
        }
    }
}
