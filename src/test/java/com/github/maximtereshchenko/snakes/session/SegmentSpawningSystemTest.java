package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class SegmentSpawningSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = mock();
    private final SegmentSpawningSystem segmentSpawningSystem =
        new SegmentSpawningSystem(dominion, entityFactory);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            Head.INSTANCE,
            new Position(0, 0)
        );
        dominion.createEntity(new SegmentTimerDefinition(0, 0));
        segmentSpawningSystem.run(0);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnStartedEvent_thenSegmentSpawned() {
        dominion.createEntity(
            Head.INSTANCE,
            new Position(1, 1)
        );
        dominion.createEntity(new SegmentTimerDefinition(1, 1));
        dominion.createEntity(TurnStarted.INSTANCE);
        segmentSpawningSystem.run(0);
        verify(entityFactory).createSegment(dominion, new Position(1, 1), 1);
    }
}