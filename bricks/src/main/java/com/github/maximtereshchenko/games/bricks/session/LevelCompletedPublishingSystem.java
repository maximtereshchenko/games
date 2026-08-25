package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelCompleted;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class LevelCompletedPublishingSystem implements System {

    private final Iterable<Entity> brickOrIncrementStarsEntities;
    private final Iterable<Entity> collectedStarsEntities;
    private final EventBus<Event> eventBus;
    private final String difficulty;
    private final int level;

    LevelCompletedPublishingSystem(
        Registry registry,
        EventBus<Event> eventBus,
        String difficulty,
        int level
    ) {
        this.brickOrIncrementStarsEntities = registry.view(
            new Query().one(Brick.class, IncrementStarsBonus.class)
        );
        this.collectedStarsEntities = registry.view(
            new Query().all(CollectedStars.class)
        );
        this.eventBus = eventBus;
        this.difficulty = difficulty;
        this.level = level;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (brickOrIncrementStarsEntities.iterator().hasNext()) {
            return;
        }
        for (var collectedStarsEntity : collectedStarsEntities) {
            var collectedStars = collectedStarsEntity.component(
                CollectedStars.class
            );
            eventBus.publish(
                new LevelCompleted(
                    difficulty,
                    level,
                    collectedStars.value
                )
            );
        }
    }
}
