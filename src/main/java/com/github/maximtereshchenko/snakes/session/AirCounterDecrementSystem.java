package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class AirCounterDecrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> airCounterEntities;

    AirCounterDecrementSystem(World world) {
        super(world);
        this.airCounterEntities = world.entities(
            new Query().all(AirCounter.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : airCounterEntities) {
            entity.component(AirCounter.class).value--;
        }
    }
}
