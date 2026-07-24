package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class HeadCollisionTargetSystem extends TurnBasedSystem {

    private final Dominion dominion;

    HeadCollisionTargetSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var headResult : dominion.findEntitiesWith(Head.class, Position.class)) {
            for (var targetResult : dominion.findEntitiesWith(Position.class).without(Head.class, HeadCollisionTarget.class)) {
                if (headResult.comp2().equals(targetResult.comp())) {
                    targetResult.entity().add(HeadCollisionTarget.INSTANCE);
                }
            }
        }
    }
}
