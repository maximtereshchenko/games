package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

final class TurnStartSystem implements Runnable {

    private final Dominion dominion;
    private final Scheduler scheduler;
    private final EntityFactory entityFactory;
    private final double turnLengthSeconds;

    TurnStartSystem(
        Dominion dominion,
        Scheduler scheduler,
        EntityFactory entityFactory,
        double turnLengthSeconds
    ) {
        this.dominion = dominion;
        this.scheduler = scheduler;
        this.entityFactory = entityFactory;
        this.turnLengthSeconds = turnLengthSeconds;
    }

    @Override
    public void run() {
        for (var stopwatch : dominion.findCompositionsWith(Stopwatch.class)) {
            stopwatch.seconds += scheduler.deltaTime();
            if (stopwatch.seconds > turnLengthSeconds) {
                entityFactory.createTurnStartedEvent();
                stopwatch.seconds -= turnLengthSeconds;
            }
        }
    }
}
