package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class SessionEndSystem extends TurnBasedSystem {

    private final Iterable<Entity> headCollisionTargetEntities;
    private final Iterable<Entity> airCounterEntities;
    private final Iterable<Entity> sessionEntities;

    SessionEndSystem(World world) {
        super(world);
        this.headCollisionTargetEntities = world.entities(
            new Query()
                .all(HeadCollisionTarget.class)
                .one(Segment.class, Wall.class)
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
            if (collision() || noAir()) {
                entity.component(Session.class).status = Session.Status.ENDED;
            }
        }
    }

    private boolean collision() {
        return headCollisionTargetEntities.iterator().hasNext();
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
