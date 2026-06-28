package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class InitialSegmentTimerSystemTest {

    private final Dominion dominion = Dominion.create();
    private final InitialSegmentTimerSystem initialSegmentTimerSystem = new InitialSegmentTimerSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new InitialSegmentTimer(0));
        dominion.createEntity(AppleEaten.INSTANCE);
        initialSegmentTimerSystem.run();
        assertThat(dominion.findCompositionsWith(InitialSegmentTimer.class))
            .singleElement()
            .extracting(initialSegmentTimer -> initialSegmentTimer.value)
            .isEqualTo(0);
    }

    @Test
    void givenTurnStartedEvent_thenInitialSegmentTimerIncremented() {
        dominion.createEntity(new InitialSegmentTimer(0));
        dominion.createEntity(AppleEaten.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        initialSegmentTimerSystem.run();
        assertThat(dominion.findCompositionsWith(InitialSegmentTimer.class))
            .singleElement()
            .extracting(initialSegmentTimer -> initialSegmentTimer.value)
            .isEqualTo(1);
    }

    @Test
    void givenNoAppleEatenEvent_thenNoChanges() {
        dominion.createEntity(new InitialSegmentTimer(0));
        dominion.createEntity(TurnStarted.INSTANCE);
        initialSegmentTimerSystem.run();
        assertThat(dominion.findCompositionsWith(InitialSegmentTimer.class))
            .singleElement()
            .extracting(initialSegmentTimer -> initialSegmentTimer.value)
            .isEqualTo(0);
    }
}