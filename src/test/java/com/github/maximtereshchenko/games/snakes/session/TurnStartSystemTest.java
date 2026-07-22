package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class TurnStartSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = mock();
    private final Mode mode = mock();
    private final TurnStartSystem turnStartSystem =
        new TurnStartSystem(dominion, entityFactory, mode);

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        when(mode.gameInterval()).thenReturn(1f);
        dominion.createEntity(new TurnTimer());
        turnStartSystem.run(0.5f);
        assertThat(dominion.findCompositionsWith(TurnTimer.class))
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnTimerGreaterThatTurnLength_thenTurnStartedEvent() {
        when(mode.gameInterval()).thenReturn(1f);
        dominion.createEntity(new TurnTimer());
        turnStartSystem.run(1.5f);
        assertThat(dominion.findCompositionsWith(TurnTimer.class))
            .extracting(stopwatch -> stopwatch.seconds)
            .containsExactly(0.5);
        verify(entityFactory).createTurnStartedEvent(dominion);
    }
}