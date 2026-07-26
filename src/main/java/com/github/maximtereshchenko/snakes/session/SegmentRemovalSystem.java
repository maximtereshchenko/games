package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SegmentRemovalSystem extends TurnBasedSystem {

    private final Dominion dominion;

    SegmentRemovalSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Timer.class, Segment.class)) {
            if (result.comp1().turnsRemaining == 0) {
                dominion.deleteEntity(result.entity());
            }
        }
    }
}
