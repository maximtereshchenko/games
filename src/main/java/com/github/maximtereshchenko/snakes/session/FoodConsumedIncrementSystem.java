package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

final class FoodConsumedIncrementSystem implements System {

    private final Iterable<Entity> statisticsEntities;

    FoodConsumedIncrementSystem(World world) {
        this.statisticsEntities = world.entities(
            new Query().all(FoodConsumed.class, Statistics.class)
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var statisticsEntity : statisticsEntities) {
            var sessionStatistics = statisticsEntity.component(Statistics.class).value;
            sessionStatistics.put(
                SessionMetric.FOOD_CONSUMED,
                sessionStatistics.get(SessionMetric.FOOD_CONSUMED) +
                statisticsEntity.component(FoodConsumed.class).value()
            );
        }
    }
}
