package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class FoodConsumedIncrementSystem implements System {

    private final Iterable<Entity> statisticsEntities;

    FoodConsumedIncrementSystem(Registry registry) {
        this.statisticsEntities = registry.view(
            new Query().all(FoodConsumed.class, Statistics.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
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
