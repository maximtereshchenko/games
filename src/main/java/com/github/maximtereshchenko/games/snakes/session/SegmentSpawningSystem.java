package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SegmentSpawningSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final EntityFactory entityFactory;

    SegmentSpawningSystem(Dominion dominion, EntityFactory entityFactory) {
        super(dominion);
        this.dominion = dominion;
        this.entityFactory = entityFactory;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Head.class, Position.class)) {
            for (var initialSegmentTimer : dominion.findCompositionsWith(SegmentTimerDefinition.class)) {
                entityFactory.createSegment(
                    dominion,
                    new Position(result.comp2()),
                    initialSegmentTimer.duration
                );
            }
        }
    }
}
