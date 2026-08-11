package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import com.github.maximtereshchenko.games.ecs.WorldEdit;

final class LeftTurnsIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> statisticsEntities;

    LeftTurnsIncrementSystem(World world) {
        super(world);
        this.statisticsEntities = world.entities(
            new Query().all(Direction.class, DirectionIntent.class, Statistics.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
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
