package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class SessionStatisticsSystem extends TurnBasedSystem {

    private final Iterable<Entity> sessionStatisticsAccumulatorEntities;
    private final Iterable<Entity> directionEntities;

    SessionStatisticsSystem(World world) {
        super(world);
        this.sessionStatisticsAccumulatorEntities = world.entities(
            new Query().all(SessionStatisticsAccumulator.class)
        );
        this.directionEntities = world.entities(
            new Query().all(CurrentForwardDirection.class, NextForwardDirection.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var accumulator : sessionStatisticsAccumulatorEntities) {
            var sessionStatistics = accumulator.component(SessionStatisticsAccumulator.class).value;
            for (var entity : directionEntities) {
                if (entity.component(CurrentForwardDirection.class).value.left() ==
                    entity.component(NextForwardDirection.class).value) {
                    sessionStatistics.put(
                        SessionStatistics.LEFT_TURNS,
                        sessionStatistics.get(SessionStatistics.LEFT_TURNS) + 1
                    );
                }
            }
        }
    }
}
