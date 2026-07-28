package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class SegmentRemainingTurnsIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> segmentDefinitionEntities;
    private final Iterable<Entity> segmentEntities;

    SegmentRemainingTurnsIncrementSystem(World world) {
        super(world);
        this.foodConsumedEntities = world.entities(
            new Query().all(FoodConsumed.class)
        );
        this.segmentDefinitionEntities = world.entities(
            new Query().all(SegmentDefinition.class)
        );
        this.segmentEntities = world.entities(
            new Query().all(Segment.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var _ : foodConsumedEntities) {
            for (var segmentDefinitionEntity : segmentDefinitionEntities) {
                var segmentDefinition = segmentDefinitionEntity.component(SegmentDefinition.class);
                segmentDefinition.durationTurns += segmentDefinition.incrementStepTurns;
                for (var segmentEntity : segmentEntities) {
                    segmentEntity.component(Segment.class)
                        .remainingTurns += segmentDefinition.incrementStepTurns;
                }
            }
        }
    }
}
