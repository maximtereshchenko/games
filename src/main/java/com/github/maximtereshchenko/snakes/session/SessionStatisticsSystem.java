package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class SessionStatisticsSystem extends TurnBasedSystem {

    private final Iterable<Entity> sessionStatisticsAccumulatorEntities;
    private final Iterable<Entity> movementEntities;

    SessionStatisticsSystem(World world) {
        super(world);
        this.sessionStatisticsAccumulatorEntities = world.entities(
            new Query().all(SessionStatisticsAccumulator.class)
        );
        this.movementEntities = world.entities(
            new Query().all(ForwardMovement.class, PlannedMovement.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var accumulator : sessionStatisticsAccumulatorEntities) {
            var sessionStatistics = accumulator.component(SessionStatisticsAccumulator.class).value;
            for (var entity : movementEntities) {
                if (entity.component(ForwardMovement.class).direction.left() ==
                    entity.component(PlannedMovement.class).direction) {
                    sessionStatistics.put(
                        SessionStatistics.LEFT_TURNS,
                        sessionStatistics.get(SessionStatistics.LEFT_TURNS) + 1
                    );
                }
            }
        }
    }
}
