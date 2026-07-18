package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentRemovalSystemTest {

    private final Dominion dominion = Dominion.create();
    private final SegmentRemovalSystem timerRemovalSystem = new SegmentRemovalSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Timer(0), Segment.INSTANCE);
        timerRemovalSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsLeft)
            .isEqualTo(0);
    }

    @Test
    void givenTurnStartedEvent_thenTimerRemoved() {
        dominion.createEntity(new Timer(0), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        timerRemovalSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class)).isEmpty();
    }

    @Test
    void givenTimerPositive_thenNoChanges() {
        dominion.createEntity(new Timer(1), Segment.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        timerRemovalSystem.run(0);
        assertThat(dominion.findCompositionsWith(Timer.class))
            .singleElement()
            .extracting(timer -> timer.turnsLeft)
            .isEqualTo(1);
    }
}