package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

abstract class TurnBasedSystem implements System {

    private final Dominion dominion;

    TurnBasedSystem(Dominion dominion) {
        this.dominion = dominion;
    }

    @Override
    public final void run(float deltaTime) {
        if (dominion.findCompositionsWith(TurnStarted.class).iterator().hasNext()) {
            onTurnStarted();
        }
    }

    abstract void onTurnStarted();
}
