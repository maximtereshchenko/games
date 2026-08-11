package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class LeftTurnsIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> statisticsEntities;

    LeftTurnsIncrementSystem(Registry registry) {
        super(registry);
        this.statisticsEntities = registry.entities(
            new Query().all(Direction.class, DirectionIntent.class, Statistics.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var statisticsEntity : statisticsEntities) {
            var statistics = statisticsEntity.component(Statistics.class).value;
            var currentDirection = statisticsEntity.component(Direction.class);
            var intentDirection = statisticsEntity.component(DirectionIntent.class).value;
            if (currentDirection.left() == intentDirection) {
                statistics.put(
                    SessionMetric.LEFT_TURNS,
                    statistics.get(SessionMetric.LEFT_TURNS) + 1
                );
            }
        }
    }
}
