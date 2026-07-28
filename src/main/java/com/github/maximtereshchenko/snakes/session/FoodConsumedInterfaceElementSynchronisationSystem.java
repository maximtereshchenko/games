package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;

import java.util.List;

final class FoodConsumedInterfaceElementSynchronisationSystem
    extends LocalizableInterfaceTextSystem {

    private final Iterable<Entity> statisticsEntities;

    FoodConsumedInterfaceElementSynchronisationSystem(World world) {
        super(world, FoodConsumedInterfaceElement.class);
        this.statisticsEntities = world.entities(
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
