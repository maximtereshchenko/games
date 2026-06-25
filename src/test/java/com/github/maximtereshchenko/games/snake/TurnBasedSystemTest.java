package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class TurnBasedSystemTest {

    private final Dominion dominion = Dominion.create();
    private final Scheduler scheduler = mock(Scheduler.class);
    private final TurnBasedSystem turnBasedSystem = new TurnBasedSystem(
        dominion,
        scheduler,
        1
    );

    @BeforeEach
    void setUp() {
        dominion.createEntity(new Stopwatch());
    }

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        when(scheduler.deltaTime()).thenReturn(0.5);
        turnBasedSystem.run();
        assertThat(dominion.findEntitiesWith(Stopwatch.class))
            .extracting(Results.With1::comp)
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        assertThat(dominion.findEntitiesWith(Event.class, TurnStarted.class))
            .isEmpty();
    }

    @Test
    void givenStopwatchGreaterThatTurnLength_thenTurnStartedEvent() {
        when(scheduler.deltaTime()).thenReturn(1.5);
        turnBasedSystem.run();
        assertThat(dominion.findEntitiesWith(Stopwatch.class))
            .extracting(Results.With1::comp)
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        assertThat(dominion.findEntitiesWith(Event.class, TurnStarted.class))
            .hasSize(1);
    }
}