package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class SegmentRemainingTurnsIncrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> segmentDefinitionEntities;
    private final Iterable<Entity> segmentEntities;

    SegmentRemainingTurnsIncrementSystem(Registry registry) {
        super(registry);
        this.foodConsumedEntities = registry.entities(
            new Query().all(FoodConsumed.class)
        );
        this.segmentDefinitionEntities = registry.entities(
            new Query().all(SegmentPolicy.class)
        );
        this.segmentEntities = registry.entities(
            new Query().all(Segment.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
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
