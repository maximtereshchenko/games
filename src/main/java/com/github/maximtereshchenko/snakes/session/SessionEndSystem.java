package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class SessionEndSystem extends TurnBasedSystem {

    private final Iterable<Entity> segmentEntities;
    private final Iterable<Entity> airCounterEntities;
    private final Iterable<Entity> sessionEntities;

    SessionEndSystem(World world) {
        super(world);
        this.segmentEntities = world.entities(
            new Query().all(Segment.class, HeadCollisionTarget.class)
        );
        this.airCounterEntities = world.entities(
            new Query().all(AirCounter.class)
        );
        this.sessionEntities = world.entities(
            new Query().all(Session.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : sessionEntities) {
            if (segmentCollision() || noAir()) {
                entity.component(Session.class).status = Session.Status.ENDED;
            }
        }
    }

    private boolean segmentCollision() {
        return segmentEntities.iterator().hasNext();
    }

    private boolean noAir() {
        for (var airCounterEntity : airCounterEntities) {
            if (airCounterEntity.component(AirCounter.class).value <= 0) {
                return true;
            }
        }
        return false;
    }
}
