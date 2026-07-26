package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadCollisionTargetRemovalSystemTest {

    private final Dominion dominion = Dominion.create();
    private final HeadCollisionTargetRemovalSystem headCollisionTargetRemovalSystem =
        new HeadCollisionTargetRemovalSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Position(0, 0), HeadCollisionTarget.INSTANCE);
        var before = dominion.findAllEntities().stream().toList();
        headCollisionTargetRemovalSystem.run(0);
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenHeadCollisionTarget_thenTagRemoved() {
        dominion.createEntity(new Position(0, 0), HeadCollisionTarget.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        headCollisionTargetRemovalSystem.run(0);
        assertThat(dominion.findEntitiesWith(Position.class))
            .extracting(Results.With1::entity)
            .singleElement()
            .doesNotMatch(entity -> entity.has(HeadCollisionTarget.class));
    }
}