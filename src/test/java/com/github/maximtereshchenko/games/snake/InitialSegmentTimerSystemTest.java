package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class InitialSegmentTimerSystemTest {

    private final Dominion dominion = Dominion.create();
    private final InitialSegmentTimerSystem initialSegmentTimerSystem = new InitialSegmentTimerSystem(
        dominion,
        2
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new InitialSegmentTimer(1));
        dominion.createEntity(FoodEaten.INSTANCE);
        initialSegmentTimerSystem.run(0);
        assertThat(dominion.findCompositionsWith(InitialSegmentTimer.class))
            .singleElement()
            .extracting(initialSegmentTimer -> initialSegmentTimer.value)
            .isEqualTo(1);
    }

    @Test
    void givenFoodEatenEvent_thenInitialSegmentTimerIncremented() {
        dominion.createEntity(new InitialSegmentTimer(1));
        dominion.createEntity(FoodEaten.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        initialSegmentTimerSystem.run(0);
        assertThat(dominion.findCompositionsWith(InitialSegmentTimer.class))
            .singleElement()
            .extracting(initialSegmentTimer -> initialSegmentTimer.value)
            .isEqualTo(3);
    }

    @Test
    void givenNoFoodEatenEvent_thenNoChanges() {
        dominion.createEntity(new InitialSegmentTimer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        initialSegmentTimerSystem.run(0);
        assertThat(dominion.findCompositionsWith(InitialSegmentTimer.class))
            .singleElement()
            .extracting(initialSegmentTimer -> initialSegmentTimer.value)
            .isEqualTo(1);
    }
}