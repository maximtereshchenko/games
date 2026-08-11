package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;

import java.util.List;

final class FoodConsumedInterfaceElementSynchronisationSystem
    extends LocalizableInterfaceTextSystem {

    private final Iterable<Entity> statisticsEntities;

    FoodConsumedInterfaceElementSynchronisationSystem(Registry registry) {
        super(registry, FoodConsumedInterfaceElement.class);
        this.statisticsEntities = registry.entities(
            new Query().all(Statistics.class)
        );
    }

    @Override
    void addVariables(List<Object> variables) {
        for (var statisticsEntity : statisticsEntities) {
            variables.add(
                statisticsEntity.component(Statistics.class)
                    .value
                    .get(SessionMetric.FOOD_CONSUMED)
            );
        }
    }
}
