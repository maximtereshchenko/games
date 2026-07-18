package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class TurnStartSystem implements System {

    private final Dominion dominion;
    private final EntityFactory entityFactory;
    private final double turnLengthSeconds;

    TurnStartSystem(
        Dominion dominion,
        EntityFactory entityFactory,
        double turnLengthSeconds
    ) {
        this.dominion = dominion;
        this.entityFactory = entityFactory;
        this.turnLengthSeconds = turnLengthSeconds;
    }

    @Override
    public void run(float deltaTime) {
        for (var turnTimer : dominion.findCompositionsWith(TurnTimer.class)) {
            turnTimer.seconds += deltaTime;
            if (turnTimer.seconds > turnLengthSeconds) {
                entityFactory.createTurnStartedEvent(dominion);
                turnTimer.seconds -= turnLengthSeconds;
            }
        }
    }
}
