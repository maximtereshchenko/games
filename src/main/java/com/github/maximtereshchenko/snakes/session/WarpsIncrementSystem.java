package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class WarpsIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> statisticsEntities;

    WarpsIncrementSystem(World world) {
        super(world);
        this.statisticsEntities = world.entities(
            new Query().all(Warped.class, Statistics.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var statisticsEntity : statisticsEntities) {
            var statistics = statisticsEntity.component(Statistics.class).value;
            statistics.put(
                SessionMetric.WARPS,
                statistics.get(SessionMetric.WARPS) + 1
            );
        }
    }
}
