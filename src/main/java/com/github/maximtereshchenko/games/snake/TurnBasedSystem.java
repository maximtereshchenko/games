package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

final class TurnBasedSystem implements Runnable {

    private final Dominion dominion;
    private final Scheduler scheduler;
    private final double turnLengthSeconds;

    TurnBasedSystem(Dominion dominion, Scheduler scheduler, double turnLengthSeconds) {
        this.dominion = dominion;
        this.scheduler = scheduler;
        this.turnLengthSeconds = turnLengthSeconds;
    }

    @Override
    public void run() {
        for (var result : dominion.findEntitiesWith(Stopwatch.class)) {
            var stopwatch = result.comp();
            stopwatch.seconds += scheduler.deltaTime();
            if (stopwatch.seconds > turnLengthSeconds) {
                dominion.createEntity(Event.INSTANCE, TurnStarted.INSTANCE);
                stopwatch.seconds -= turnLengthSeconds;
            }
        }
    }
}
