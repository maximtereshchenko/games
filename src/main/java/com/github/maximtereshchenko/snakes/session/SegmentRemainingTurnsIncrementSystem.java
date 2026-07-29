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
            new Query().all(SegmentPolicy.class)
        );
        this.segmentEntities = world.entities(
            new Query().all(Segment.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var foodConsumedEntity : foodConsumedEntities) {
            var foodConsumed = foodConsumedEntity.component(FoodConsumed.class)
                .value();
            for (var segmentDefinitionEntity : segmentDefinitionEntities) {
                var segmentDefinition = segmentDefinitionEntity.component(SegmentPolicy.class);
                var increment = segmentDefinition.incrementStepTurns * foodConsumed;
                segmentDefinition.durationTurns += increment;
                for (var segmentEntity : segmentEntities) {
                    var segment = segmentEntity.component(Segment.class);
                    segment.remainingTurns += increment;
                }
            }
        }
    }
}
