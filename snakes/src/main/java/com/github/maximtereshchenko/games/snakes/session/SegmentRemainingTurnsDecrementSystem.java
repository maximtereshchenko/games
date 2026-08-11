package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class SegmentRemainingTurnsDecrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> segmentEntities;

    SegmentRemainingTurnsDecrementSystem(Registry registry) {
        super(registry);
        this.segmentEntities = registry.entities(
            new Query().all(Segment.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var segmentEntity : segmentEntities) {
            var segment = segmentEntity.component(Segment.class);
            segment.remainingTurns--;
            if (segment.remainingTurns == 0) {
                registryEdit.deleteEntity(segmentEntity.id());
            }
        }
    }
}
