package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class WarpsIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> statisticsEntities;

    WarpsIncrementSystem(Registry registry) {
        super(registry);
        this.statisticsEntities = registry.entities(
            new Query().all(Warped.class, Statistics.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var statisticsEntity : statisticsEntities) {
            var statistics = statisticsEntity.component(Statistics.class).value;
            statistics.put(
                SessionMetric.WARPS,
                statistics.get(SessionMetric.WARPS) + 1
            );
        }
    }
}
