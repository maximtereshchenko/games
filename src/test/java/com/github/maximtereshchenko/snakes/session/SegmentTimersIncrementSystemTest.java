package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentTimersIncrementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final SegmentTimersIncrementSystem segmentTimersIncrementSystem =
        new SegmentTimersIncrementSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new SegmentTimerDefinition(1, 1));
        dominion.createEntity(new Timer(1, 1), Segment.INSTANCE);
        segmentTimersIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(SegmentTimerDefinition.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new SegmentTimerDefinition(1, 1));
        assertThat(dominion.findEntitiesWith(Timer.class, Segment.class))
            .singleElement()
            .extracting(Results.With2::comp1)
            .usingRecursiveComparison()
            .isEqualTo(new Timer(1, 1));
    }

    @Test
    void givenNoFoodEatenEvent_thenNoChanges() {
        dominion.createEntity(new SegmentTimerDefinition(1, 1));
        dominion.createEntity(new Timer(1, 1), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        segmentTimersIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(SegmentTimerDefinition.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new SegmentTimerDefinition(1, 1));
        assertThat(dominion.findEntitiesWith(Timer.class, Segment.class))
            .singleElement()
            .extracting(Results.With2::comp1)
            .usingRecursiveComparison()
            .isEqualTo(new Timer(1, 1));
    }

    @Test
    void givenFoodEatenEvent_thenTimerIncremented() {
        dominion.createEntity(new SegmentTimerDefinition(1, 1));
        dominion.createEntity(new Timer(1, 1), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        dominion.createEntity(FoodEaten.INSTANCE);
        segmentTimersIncrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(SegmentTimerDefinition.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new SegmentTimerDefinition(1, 2));
        assertThat(dominion.findEntitiesWith(Timer.class, Segment.class))
            .singleElement()
            .extracting(Results.With2::comp1)
            .usingRecursiveComparison()
            .isEqualTo(new Timer(1, 2));
    }
}