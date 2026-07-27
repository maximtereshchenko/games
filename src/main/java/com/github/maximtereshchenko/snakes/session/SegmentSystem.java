package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;

final class SegmentSystem extends OnFoodEatenEventSystem {

    private final Iterable<Entity> segmentDefinitionEntities;
    private final Iterable<Entity> segmentEntities;

    SegmentSystem(World world) {
        super(world);
        this.segmentDefinitionEntities = world.entities(
            new Query().all(SegmentDefinition.class)
        );
        this.segmentEntities = world.entities(
            new Query().all(Segment.class)
        );
    }

    @Override
    void onFoodEaten() {
        for (var definition : segmentDefinitionEntities) {
            var initialSegmentTimer = definition.component(SegmentDefinition.class);
            initialSegmentTimer.durationTurns += initialSegmentTimer.incrementStepTurns;
            for (var segment : segmentEntities) {
                segment.component(Segment.class).remainingTurns +=
                    initialSegmentTimer.incrementStepTurns;
            }
        }
    }
}
