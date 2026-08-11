package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import com.github.maximtereshchenko.games.ecs.WorldEdit;

final class SegmentSpawningSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> segmentDefinitionEntities;

    SegmentSpawningSystem(World world) {
        super(world);
        this.headEntities = world.entities(
            new Query()
                .all(Head.class, WorldPosition.class)
        );
        this.segmentDefinitionEntities = world.entities(
            new Query().all(SegmentPolicy.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var headEntity : headEntities) {
            var worldPosition = headEntity.component(WorldPosition.class);
            for (var segmentDefinitionEntity : segmentDefinitionEntities) {
                var segmentWorldPosition = new WorldPosition();
                segmentWorldPosition.copy(worldPosition);
                worldEdit.addComponents(
                    worldEdit.createEntity(),
                    new Segment(
                        segmentDefinitionEntity.component(SegmentPolicy.class)
                            .durationTurns
                    ),
                    segmentWorldPosition,
                    PaletteColor.SEGMENT,
                    new Opacity(1)
                );
            }
        }
    }
}
