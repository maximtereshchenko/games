package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SessionEndSystemTest {

    private final Dominion dominion = Dominion.create();
    private final SessionEndSystem sessionEndSystem = new SessionEndSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Session(Session.Status.RUNNING));
        sessionEndSystem.run(0);
        assertThat(dominion.findCompositionsWith(Session.class))
            .extracting(game -> game.status)
            .containsExactly(Session.Status.RUNNING);
    }

    @Test
    void givenNoHeadOnSegment_thenSessionRunning() {
        dominion.createEntity(new Session(Session.Status.RUNNING));
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Segment.INSTANCE, new Position(1, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        sessionEndSystem.run(0);
        assertThat(dominion.findCompositionsWith(Session.class))
            .extracting(game -> game.status)
            .containsExactly(Session.Status.RUNNING);
    }

    @Test
    void givenHeadOnSegment_thenSessionEnded() {
        dominion.createEntity(new Session(Session.Status.RUNNING));
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Segment.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        sessionEndSystem.run(0);
        assertThat(dominion.findCompositionsWith(Session.class))
            .extracting(game -> game.status)
            .containsExactly(Session.Status.ENDED);
    }
}