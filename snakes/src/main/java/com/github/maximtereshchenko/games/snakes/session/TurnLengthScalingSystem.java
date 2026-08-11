package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class TurnLengthScalingSystem extends TurnBasedSystem {

    private final Iterable<Entity> turnTimerEntities;
    private final Iterable<Entity> statisticsEntities;

    TurnLengthScalingSystem(Registry registry) {
        super(registry);
        this.turnTimerEntities = registry.entities(
            new Query().all(TurnTimer.class, TurnLengthScaling.class)
        );
        this.statisticsEntities = registry.entities(
            new Query().all(Statistics.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
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

    private float turnLength(float base, float reduction, int foodConsumed, int step) {
        var steps = foodConsumed / step;
        return base - reduction * steps;
    }
}
