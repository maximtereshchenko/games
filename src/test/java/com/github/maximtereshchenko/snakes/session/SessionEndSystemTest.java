package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SessionEndSystemTest {

    private final Dominion dominion = Dominion.create();
    private final SessionEndSystem sessionEndSystem = new SessionEndSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(Segment.INSTANCE, HeadCollisionTarget.INSTANCE);
        dominion.createEntity(new Session(Session.Status.RUNNING));
        sessionEndSystem.run(0);
        assertThat(dominion.findCompositionsWith(Session.class))
            .extracting(game -> game.status)
            .containsExactly(Session.Status.RUNNING);
    }

    @Test
    void givenSegmentCollisionTarget_thenSessionEnded() {
        dominion.createEntity(new Session(Session.Status.RUNNING));
        dominion.createEntity(Segment.INSTANCE, HeadCollisionTarget.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        sessionEndSystem.run(0);
        assertThat(dominion.findCompositionsWith(Session.class))
            .extracting(game -> game.status)
            .containsExactly(Session.Status.ENDED);
    }
}