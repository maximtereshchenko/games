package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class TailRemovalSystem extends TurnBasedSystem {

    private final Dominion dominion;

    TailRemovalSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Tail.class, Next.class)) {
            dominion.deleteEntity(result.entity());
            var next = result.comp2().entity;
            next.removeType(Previous.class);
            next.add(Tail.INSTANCE);
        }
    }
}
