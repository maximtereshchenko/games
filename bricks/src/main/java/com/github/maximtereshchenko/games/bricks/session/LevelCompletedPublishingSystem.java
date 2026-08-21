package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelCompleted;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;
import com.github.maximtereshchenko.games.event.EventBus;

final class LevelCompletedPublishingSystem implements System {

    private final Iterable<Entity> brickOrIncrementStarsEntities;
    private final Iterable<Entity> collectedStarsEntities;
    private final EventBus<Event> eventBus;

    LevelCompletedPublishingSystem(
        Registry registry,
        EventBus<Event> eventBus
    ) {
        this.brickOrIncrementStarsEntities = registry.entities(
            new Query().one(Brick.class, IncrementStarsBonus.class)
        );
        this.collectedStarsEntities = registry.entities(
            new Query().all(CollectedStars.class)
        );
        this.eventBus = eventBus;
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
            eventBus.publish(new LevelCompleted(collectedStars.value));
        }
    }
}
