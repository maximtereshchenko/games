package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class TurnStartSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = mock();
    private final TurnStartSystem turnStartSystem =
        new TurnStartSystem(dominion, entityFactory);

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        dominion.createEntity(new TurnTimer(1.5f, 0.5f));
        turnStartSystem.run(0.5f);
        assertThat(dominion.findCompositionsWith(TurnTimer.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(1.5f, 1.0f));
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnTimerGreaterThatTurnLength_thenTurnStartedEvent() {
        dominion.createEntity(new TurnTimer(0.3f, 0.2f));
        turnStartSystem.run(0.4f);
        assertThat(dominion.findCompositionsWith(TurnTimer.class))
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.3f, 0.3f));
        verify(entityFactory).createTurnStartedEvent(dominion);
    }
}