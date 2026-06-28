package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentSpawningSystemTest {

    private final Dominion dominion = Dominion.create();
    private final SegmentSpawningSystem segmentSpawningSystem = new SegmentSpawningSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            Head.INSTANCE,
            new Position(0, 0)
        );
        dominion.createEntity(new InitialSegmentTimer(0));
        var before = dominion.findAllEntities().stream().toList();
        segmentSpawningSystem.run();
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenTurnStartedEvent_thenSegmentSpawned() {
        dominion.createEntity(
            Head.INSTANCE,
            new Position(1, 1)
        );
        dominion.createEntity(new InitialSegmentTimer(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        segmentSpawningSystem.run();
        assertThat(dominion.findEntitiesWith(Timer.class, Position.class, Visible.class))
            .singleElement()
            .extracting(
                result -> result.comp1().value,
                Results.With3::comp2,
                result -> result.comp3().color()
            )
            .containsExactly(1, new Position(1, 1), Colors.SEGMENT);
    }
}