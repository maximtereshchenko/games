package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class SegmentSpawningSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> segmentDefinitionEntities;

    SegmentSpawningSystem(Registry registry) {
        super(registry);
        this.headEntities = registry.view(
            new Query()
                .all(Head.class, WorldPosition.class)
        );
        this.segmentDefinitionEntities = registry.view(
            new Query().all(SegmentPolicy.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var headEntity : headEntities) {
            var worldPosition = headEntity.component(WorldPosition.class);
            for (var segmentDefinitionEntity : segmentDefinitionEntities) {
                var segmentWorldPosition = new WorldPosition();
                segmentWorldPosition.copy(worldPosition);
                registryEdit.addComponents(
                    registryEdit.createEntity(),
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
