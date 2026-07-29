package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class TurnLengthScalingSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> turnTimerEntities;
    private final Iterable<Entity> statisticsEntities;

    TurnLengthScalingSystem(World world) {
        super(world);
        this.foodConsumedEntities = world.entities(
            new Query().all(FoodConsumed.class)
        );
        this.turnTimerEntities = world.entities(
            new Query().all(TurnTimer.class, TurnLengthScaling.class)
        );
        this.statisticsEntities = world.entities(
            new Query().all(Statistics.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var _ : foodConsumedEntities) {
            for (var turnTimerEntity : turnTimerEntities) {
                for (var statisticsEntity : statisticsEntities) {
                    var foodConsumed = statisticsEntity.component(Statistics.class)
                        .value
                        .get(SessionMetric.FOOD_CONSUMED);
                    var turnLengthScaling = turnTimerEntity.component(TurnLengthScaling.class);
                    turnTimerEntity.component(TurnTimer.class)
                        .turnLengthSeconds = Math.max(
                        turnLengthScaling.minimalTurnLengthSeconds(),
                        turnLength(
                            turnLengthScaling.baseTurnLengthSeconds(),
                            turnLengthScaling.turnLengthReductionSeconds(),
                            foodConsumed,
                            turnLengthScaling.foodConsumedStep()
                        )
                    );
                }
            }
        }
    }

    private float turnLength(float base, float reduction, int foodConsumed, int step) {
        var steps = foodConsumed / step;
        return base - reduction * steps;
    }
}
