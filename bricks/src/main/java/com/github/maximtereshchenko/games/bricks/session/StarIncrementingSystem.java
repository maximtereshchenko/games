package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class StarIncrementingSystem implements System {

    private final Iterable<Entity> incrementStarEntities;
    private final Iterable<Entity> starCounterEntities;

    StarIncrementingSystem(Registry registry) {
        this.incrementStarEntities = registry.entities(
            new Query().all(IncrementStarsBonus.class, Activated.class)
        );
        this.starCounterEntities = registry.entities(
            new Query().all(StarCounter.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var _ : incrementStarEntities) {
            for (var starConterEntity : starCounterEntities) {
                var starCounter = starConterEntity.component(
                    StarCounter.class
                );
                starCounter.value++;
            }
        }
    }
}
