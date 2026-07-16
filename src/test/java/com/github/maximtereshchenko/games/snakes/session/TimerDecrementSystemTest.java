package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TimerDecrementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final TimerDecrementSystem timerDecrementSystem = new TimerDecrementSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1));
        timerDecrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(1);
    }

    @Test
    void givenTurnStartedEvent_thenTimerDecremented() {
        dominion.createEntity(new Timer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        timerDecrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(0);
    }
}