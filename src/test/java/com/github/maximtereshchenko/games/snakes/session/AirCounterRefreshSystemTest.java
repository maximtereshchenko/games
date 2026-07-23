package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirCounterRefreshSystemTest {

    private final Dominion dominion = Dominion.create();
    private final AirCounterRefreshSystem airCounterRefreshSystem =
        new AirCounterRefreshSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new AirCounter(2, 1));
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Air.INSTANCE, new Position(0, 0));
        airCounterRefreshSystem.run(0);
        assertThat(dominion.findCompositionsWith(AirCounter.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 1));
    }

    @Test
    void givenNoHeadOnAir_thenNoChanges() {
        dominion.createEntity(new AirCounter(2, 1));
        dominion.createEntity(Head.INSTANCE, new Position(1, 1));
        dominion.createEntity(Air.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        airCounterRefreshSystem.run(0);
        assertThat(dominion.findCompositionsWith(AirCounter.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 1));
    }

    @Test
    void givenHeadOnAir_thenAirCounterRefreshed() {
        dominion.createEntity(new AirCounter(2, 1));
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Air.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        airCounterRefreshSystem.run(0);
        assertThat(dominion.findCompositionsWith(AirCounter.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 2));
    }
}