package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TimerSystemTest {

    private final Dominion dominion = Dominion.create();
    private final TimerSystem timerDecrementSystem = new TimerSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1, 1));
        timerDecrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsRemaining)
            .isEqualTo(1);
    }

    @Test
    void givenTurnStartedEvent_thenTimerDecremented() {
        dominion.createEntity(new Timer(1, 1));
        dominion.createEntity(TurnStarted.INSTANCE);
        timerDecrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsRemaining)
            .isEqualTo(0);
    }
}