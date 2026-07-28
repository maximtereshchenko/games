package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodConsumedInterfaceElementSynchronisationSystem extends TurnBasedSystem {

    private final Iterable<Entity> statisticsEntities;
    private final Iterable<Entity> foodConsumedInterfaceElementEntities;

    FoodConsumedInterfaceElementSynchronisationSystem(World world) {
        super(world);
        this.statisticsEntities = world.entities(
            new Query().all(Statistics.class, FoodConsumed.class)
        );
        this.foodConsumedInterfaceElementEntities = world.entities(
            new Query().all(FoodConsumedInterfaceElement.class, InterfaceText.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var statisticsEntity : statisticsEntities) {
            for (var foodConsumedInterfaceElementEntity : foodConsumedInterfaceElementEntities) {
                foodConsumedInterfaceElementEntity.component(InterfaceText.class).value =
                    String.valueOf(
                        statisticsEntity.component(Statistics.class)
                            .value
                            .get(SessionMetric.FOOD_CONSUMED)
                    );
            }
        }
    }
}
