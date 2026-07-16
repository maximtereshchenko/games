package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class TurnStartSystem implements System {

    private final Dominion dominion;
    private final double turnLengthSeconds;

    TurnStartSystem(Dominion dominion, double turnLengthSeconds) {
        this.dominion = dominion;
        this.turnLengthSeconds = turnLengthSeconds;
    }

    @Override
    public void run(float deltaTime) {
        for (var stopwatch : dominion.findCompositionsWith(Stopwatch.class)) {
            stopwatch.seconds += deltaTime;
            if (stopwatch.seconds > turnLengthSeconds) {
                dominion.createEntity(TurnStarted.INSTANCE, Event.INSTANCE);
                stopwatch.seconds -= turnLengthSeconds;
            }
        }
    }
}
