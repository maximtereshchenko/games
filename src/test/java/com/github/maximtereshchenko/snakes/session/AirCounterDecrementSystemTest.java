package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirCounterDecrementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final AirCounterDecrementSystem airCounterDecrementSystem =
        new AirCounterDecrementSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new AirCounter(2, 1));
        airCounterDecrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(AirCounter.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 1));
    }

    @Test
    void givenTurnStartedEvent_thenAirCounterDecremented() {
        dominion.createEntity(new AirCounter(2, 1));
        dominion.createEntity(TurnStarted.INSTANCE);
        airCounterDecrementSystem.run(0);
        assertThat(dominion.findCompositionsWith(AirCounter.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 0));
    }
}