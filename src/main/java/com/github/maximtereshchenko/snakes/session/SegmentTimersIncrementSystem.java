package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;

final class SegmentTimersIncrementSystem extends OnFoodEatenEventSystem {

    private final Iterable<Entity> segmentTimerDefinitionEntities;
    private final Iterable<Entity> segmentEntities;

    SegmentTimersIncrementSystem(World world) {
        super(world);
        this.segmentTimerDefinitionEntities = world.entities(
            new Query().all(SegmentTimerDefinition.class)
        );
        this.segmentEntities = world.entities(
            new Query().all(Timer.class, Segment.class)
        );
    }

    @Override
    void onFoodEaten() {
        for (var definition : segmentTimerDefinitionEntities) {
            var initialSegmentTimer = definition.component(SegmentTimerDefinition.class);
            initialSegmentTimer.duration += initialSegmentTimer.incrementStep;
            for (var segment : segmentEntities) {
                segment.component(Timer.class).turnsRemaining +=
                    initialSegmentTimer.incrementStep;
            }
        }
    }
}
