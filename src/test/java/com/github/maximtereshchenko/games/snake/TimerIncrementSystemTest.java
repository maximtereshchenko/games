package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TimerIncrementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final TimerIncrementSystem timerIncrementSystem = new TimerIncrementSystem(
        dominion,
        2
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1));
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(1);
    }

    @Test
    void givenNoAppleEatenEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(1);
    }

    @Test
    void givenAppleEatenEvent_thenTimerIncremented() {
        dominion.createEntity(new Timer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        dominion.createEntity(AppleEaten.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(3);
    }
}