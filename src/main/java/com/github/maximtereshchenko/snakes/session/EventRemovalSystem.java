package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class EventRemovalSystem extends TurnBasedSystem {

    private final Iterable<Entity> eventEntities;

    EventRemovalSystem(World world) {
        super(world);
        this.eventEntities = world.entities(
            new Query().all(Event.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var event : eventEntities) {
            worldEdit.deleteEntity(event.id());
        }
    }
}
