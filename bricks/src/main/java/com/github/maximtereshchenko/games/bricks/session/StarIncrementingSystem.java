package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class StarIncrementingSystem implements System {

    private final Iterable<Entity> incrementStarEntities;
    private final Iterable<Entity> collectedStarsEntities;

    StarIncrementingSystem(Registry registry) {
        this.incrementStarEntities = registry.entities(
            new Query().all(IncrementStarsBonus.class, Activated.class)
        );
        this.collectedStarsEntities = registry.entities(
            new Query().all(CollectedStars.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var _ : incrementStarEntities) {
            for (var collectedStarsEntity : collectedStarsEntities) {
                var collectedStars = collectedStarsEntity.component(
                    CollectedStars.class
                );
                collectedStars.value++;
            }
        }
    }
}
