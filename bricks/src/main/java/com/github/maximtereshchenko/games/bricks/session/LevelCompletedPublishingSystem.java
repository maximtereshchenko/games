package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelCompleted;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;
import com.github.maximtereshchenko.games.event.EventBus;

final class LevelCompletedPublishingSystem implements System {

    private final Iterable<Entity> brickOrIncrementStarsEntities;
    private final Iterable<Entity> starCounterEntities;
    private final EventBus<Event> eventBus;

    LevelCompletedPublishingSystem(
        Registry registry,
        EventBus<Event> eventBus
    ) {
        this.brickOrIncrementStarsEntities = registry.entities(
            new Query().one(Brick.class, IncrementStars.class)
        );
        this.starCounterEntities = registry.entities(
            new Query().all(StarCounter.class)
        );
        this.eventBus = eventBus;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (brickOrIncrementStarsEntities.iterator().hasNext()) {
            return;
        }
        for (var starCounterEntity : starCounterEntities) {
            var starCounter = starCounterEntity.component(
                StarCounter.class
            );
            eventBus.publish(new LevelCompleted(starCounter.value));
        }
    }
}
