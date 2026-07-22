package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;

final class TurnStartSystem implements System {

    private final Dominion dominion;
    private final EntityFactory entityFactory;
    private final Mode mode;

    TurnStartSystem(
        Dominion dominion,
        EntityFactory entityFactory,
        Mode mode
    ) {
        this.dominion = dominion;
        this.entityFactory = entityFactory;
        this.mode = mode;
    }

    @Override
    public void run(float deltaTime) {
        for (var turnTimer : dominion.findCompositionsWith(TurnTimer.class)) {
            turnTimer.seconds += deltaTime;
            if (turnTimer.seconds > mode.gameInterval()) {
                entityFactory.createTurnStartedEvent(dominion);
                turnTimer.seconds -= mode.gameInterval();
            }
        }
    }
}
