package com.github.maximtereshchenko.games.snake;

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
        timerDecrementSystem.run();
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(1);
    }

    @Test
    void givenTurnStartedEvent_thenTimerDecremented() {
        dominion.createEntity(new Timer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        timerDecrementSystem.run();
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(0);
    }

    @Test
    void givenAppleEatenEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        dominion.createEntity(AppleEaten.INSTANCE);
        timerDecrementSystem.run();
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.value)
            .isEqualTo(1);
    }
}