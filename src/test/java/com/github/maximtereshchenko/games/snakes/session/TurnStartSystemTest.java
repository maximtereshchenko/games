package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TurnStartSystemTest {

    private final Dominion dominion = Dominion.create();
    private final TurnStartSystem turnStartSystem = new TurnStartSystem(
        dominion,
        1
    );

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        dominion.createEntity(new Stopwatch());
        turnStartSystem.run(0.5f);
        assertThat(dominion.findCompositionsWith(Stopwatch.class))
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        assertThat(dominion.findEntitiesWith(Event.class, TurnStarted.class))
            .isEmpty();
    }

    @Test
    void givenStopwatchGreaterThatTurnLength_thenTurnStartedEvent() {
        dominion.createEntity(new Stopwatch());
        turnStartSystem.run(1.5f);
        assertThat(dominion.findCompositionsWith(Stopwatch.class))
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        assertThat(dominion.findEntitiesWith(Event.class, TurnStarted.class))
            .hasSize(1);
    }
}