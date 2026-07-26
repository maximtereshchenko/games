package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

final class HeadCollisionTargetSystemTest {

    private final Dominion dominion = Dominion.create();
    private final HeadCollisionTargetSystem headCollisionTargetSystem =
        new HeadCollisionTargetSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(new Position(0, 0));
        headCollisionTargetSystem.run(0);
        assertThat(dominion.findEntitiesWith(Position.class).without(Head.class))
            .singleElement()
            .extracting(Results.With1::entity)
            .doesNotMatch(entity -> entity.has(HeadCollisionTarget.class));
    }

    @Test
    void givenNoHeadOnPosition_thenNoChanges() {
        dominion.createEntity(Head.INSTANCE, new Position(1, 1));
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        headCollisionTargetSystem.run(0);
        assertThat(dominion.findEntitiesWith(Position.class).without(Head.class))
            .singleElement()
            .extracting(Results.With1::entity)
            .doesNotMatch(entity -> entity.has(HeadCollisionTarget.class));
    }

    @Test
    void givenHeadOnPosition_thenHeadCollisionTargetAdded() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        headCollisionTargetSystem.run(0);
        assertThat(dominion.findEntitiesWith(Position.class, HeadCollisionTarget.class))
            .singleElement()
            .extracting(Results.With2::comp1)
            .isEqualTo(new Position(0, 0));
    }

    @Test
    void givenHeadCollisionTargetPresent_thenNoExceptionThrown() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(new Position(0, 0), HeadCollisionTarget.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        assertThatCode(() -> headCollisionTargetSystem.run(0))
            .doesNotThrowAnyException();
    }
}