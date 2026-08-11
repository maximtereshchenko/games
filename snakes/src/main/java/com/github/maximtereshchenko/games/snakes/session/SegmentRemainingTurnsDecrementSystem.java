package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import com.github.maximtereshchenko.games.ecs.WorldEdit;

final class SegmentRemainingTurnsDecrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> segmentEntities;

    SegmentRemainingTurnsDecrementSystem(World world) {
        super(world);
        this.segmentEntities = world.entities(
            new Query().all(Segment.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var segmentEntity : segmentEntities) {
            var segment = segmentEntity.component(Segment.class);
            segment.remainingTurns--;
            if (segment.remainingTurns == 0) {
                worldEdit.deleteEntity(segmentEntity.id());
            }
        }
    }
}
