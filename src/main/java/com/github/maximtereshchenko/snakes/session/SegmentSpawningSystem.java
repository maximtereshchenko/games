package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class SegmentSpawningSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> segmentTimerDefinitionEntities;
    private final EntityFactory entityFactory;

    SegmentSpawningSystem(World world, EntityFactory entityFactory) {
        super(world);
        this.headEntities = world.entities(
            new Query().all(Head.class, Position.class)
        );
        this.segmentTimerDefinitionEntities = world.entities(
            new Query().all(SegmentDefinition.class)
        );
        this.entityFactory = entityFactory;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var head : headEntities) {
            for (var definition : segmentTimerDefinitionEntities) {
                entityFactory.createSegment(
                    worldEdit,
                    new Position(head.component(Position.class)),
                    definition.component(SegmentDefinition.class).durationTurns
                );
            }
        }
    }
}
