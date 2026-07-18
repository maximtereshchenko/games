package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentTimerIncrementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final SegmentTimerIncrementSystem timerIncrementSystem =
        new SegmentTimerIncrementSystem(dominion, 2);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1), Segment.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsLeft)
            .isEqualTo(1);
    }

    @Test
    void givenNoFoodEatenEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsLeft)
            .isEqualTo(1);
    }

    @Test
    void givenFoodEatenEvent_thenTimerIncremented() {
        dominion.createEntity(new Timer(1), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        dominion.createEntity(FoodEaten.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsLeft)
            .isEqualTo(3);
    }
}