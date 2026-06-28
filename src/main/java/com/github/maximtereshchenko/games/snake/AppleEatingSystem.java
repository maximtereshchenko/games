package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

final class AppleEatingSystem extends TurnBasedSystem {

    private final Dominion dominion;

    AppleEatingSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var appleResult : dominion.findEntitiesWith(Apple.class, Position.class)) {
            for (var headResult : dominion.findEntitiesWith(Head.class, Position.class)) {
                if (headResult.comp2().equals(appleResult.comp2())) {
                    dominion.deleteEntity(appleResult.entity());
                    dominion.createEntity(AppleEaten.INSTANCE, Event.INSTANCE);
                }
            }
        }
    }
}
