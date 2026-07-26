package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class TurnStartSystem implements System {

    private final Dominion dominion;
    private final EntityFactory entityFactory;

    TurnStartSystem(
        Dominion dominion,
        EntityFactory entityFactory
    ) {
        this.dominion = dominion;
        this.entityFactory = entityFactory;
    }

    @Override
    public void run(float deltaTime) {
        for (var turnTimer : dominion.findCompositionsWith(TurnTimer.class)) {
            turnTimer.timePassedSeconds += deltaTime;
            if (turnTimer.timePassedSeconds > turnTimer.turnLengthSeconds) {
                turnTimer.timePassedSeconds -= turnTimer.turnLengthSeconds;
                entityFactory.createTurnStartedEvent(dominion);
            }
        }
    }
}
