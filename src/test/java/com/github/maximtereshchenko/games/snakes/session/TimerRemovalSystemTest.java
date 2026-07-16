package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TimerRemovalSystemTest {

    private final Dominion dominion = Dominion.create();
    private final TimerRemovalSystem timerRemovalSystem = new TimerRemovalSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Timer(0));
        timerRemovalSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(0);
    }

    @Test
    void givenTurnStartedEvent_thenTimerRemoved() {
        dominion.createEntity(new Timer(0));
        dominion.createEntity(TurnStarted.INSTANCE);
        timerRemovalSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class)).isEmpty();
    }

    @Test
    void givenTimerPositive_thenNoChanges() {
        dominion.createEntity(new Timer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        timerRemovalSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(1);
    }
}