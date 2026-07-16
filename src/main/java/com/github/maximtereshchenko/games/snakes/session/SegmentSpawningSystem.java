package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SegmentSpawningSystem extends TurnBasedSystem {

    private final Dominion dominion;

    SegmentSpawningSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Head.class, Position.class)) {
            for (var initialSegmentTimer : dominion.findCompositionsWith(InitialSegmentTimer.class)) {
                dominion.createEntity(
                    new Timer(initialSegmentTimer.value),
                    new Position(result.comp2()),
                    Colored.SEGMENT
                );
            }
        }
    }
}
