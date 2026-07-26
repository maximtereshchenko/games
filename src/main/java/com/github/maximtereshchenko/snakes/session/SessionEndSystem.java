package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class SessionEndSystem extends TurnBasedSystem {

    private final Iterable<Entity> segmentEntities;
    private final Iterable<Entity> sessionEntities;

    SessionEndSystem(World world) {
        super(world);
        this.segmentEntities = world.entities(
            new Query().all(Segment.class, HeadCollisionTarget.class)
        );
        this.sessionEntities = world.entities(
            new Query().all(Session.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        if (segmentEntities.iterator().hasNext()) {
            for (var entity : sessionEntities) {
                entity.component(Session.class).status = Session.Status.ENDED;
            }
        }
    }
}
