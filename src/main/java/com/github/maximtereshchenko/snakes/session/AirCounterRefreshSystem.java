package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class AirCounterRefreshSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> airEntities;
    private final Iterable<Entity> airCounterEntities;

    AirCounterRefreshSystem(World world) {
        super(world);
        this.headEntities = world.entities(
            new Query().all(Head.class, Position.class)
        );
        this.airEntities = world.entities(
            new Query().all(Air.class, Position.class));
        this.airCounterEntities = world.entities(
            new Query().all(AirCounter.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var head : headEntities) {
            for (var airEntity : airEntities) {
                if (head.component(Position.class).equals(airEntity.component(Position.class))) {
                    for (var counter : airCounterEntities) {
                        var airCounter = counter.component(AirCounter.class);
                        airCounter.value = airCounter.capacity;
                    }
                }
            }
        }
    }
}
