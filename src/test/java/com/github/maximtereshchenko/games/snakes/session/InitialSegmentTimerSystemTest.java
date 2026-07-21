package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.Configuration;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class InitialSegmentTimerSystemTest {

    private final Dominion dominion = Dominion.create();
    private final Configuration configuration = mock();
    private final InitialSegmentTimerSystem initialSegmentTimerSystem =
        new InitialSegmentTimerSystem(dominion, configuration);

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
        when(configuration.snakeFoodGrowth()).thenReturn(2);
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