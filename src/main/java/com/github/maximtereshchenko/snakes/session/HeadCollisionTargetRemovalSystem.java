package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class HeadCollisionTargetRemovalSystem extends TurnBasedSystem {

    private final Dominion dominion;

    HeadCollisionTargetRemovalSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(HeadCollisionTarget.class)) {
            result.entity().removeType(HeadCollisionTarget.class);
        }
    }
}
