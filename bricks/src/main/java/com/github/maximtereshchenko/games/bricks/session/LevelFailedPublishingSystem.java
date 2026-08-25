package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelFailed;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;
import com.github.maximtereshchenko.games.common.event.EventBus;

final class LevelFailedPublishingSystem implements System {

    private final Iterable<Entity> entities;
    private final EventBus<Event> eventBus;

    LevelFailedPublishingSystem(
        Registry registry,
        EventBus<Event> eventBus
    ) {
        this.entities = registry.entities(
            new Query().one(Lives.class)
        );
        this.eventBus = eventBus;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var lives = entity.component(Lives.class);
            if (lives.value == 0) {
                eventBus.publish(new LevelFailed());
            }
        }
    }
}
