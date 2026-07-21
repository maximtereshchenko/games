package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.Configuration;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class SegmentTimerIncrementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final Configuration configuration = mock();
    private final SegmentTimerIncrementSystem timerIncrementSystem =
        new SegmentTimerIncrementSystem(dominion, configuration);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1), Segment.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsRemaining)
            .isEqualTo(1);
    }

    @Test
    void givenNoFoodEatenEvent_thenNoChanges() {
        dominion.createEntity(new Timer(1), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsRemaining)
            .isEqualTo(1);
    }

    @Test
    void givenFoodEatenEvent_thenTimerIncremented() {
        when(configuration.snakeFoodGrowth()).thenReturn(2);
        dominion.createEntity(new Timer(1), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        dominion.createEntity(FoodEaten.INSTANCE);
        timerIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsRemaining)
            .isEqualTo(3);
    }
}