package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class TurnStartSystemTest {

    private final Dominion dominion = Dominion.create();
    private final Scheduler scheduler = mock(Scheduler.class);
    private final TurnStartSystem turnStartSystem = new TurnStartSystem(
        dominion,
        scheduler,
        1
    );

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        dominion.createEntity(new Stopwatch());
        when(scheduler.deltaTime()).thenReturn(0.5);
        turnStartSystem.run();
        assertThat(dominion.findCompositionsWith(Stopwatch.class))
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        assertThat(dominion.findEntitiesWith(Event.class, TurnStarted.class))
            .isEmpty();
    }

    @Test
    void givenStopwatchGreaterThatTurnLength_thenTurnStartedEvent() {
        dominion.createEntity(new Stopwatch());
        when(scheduler.deltaTime()).thenReturn(1.5);
        turnStartSystem.run();
        assertThat(dominion.findCompositionsWith(Stopwatch.class))
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        assertThat(dominion.findEntitiesWith(Event.class, TurnStarted.class))
            .hasSize(1);
    }
}