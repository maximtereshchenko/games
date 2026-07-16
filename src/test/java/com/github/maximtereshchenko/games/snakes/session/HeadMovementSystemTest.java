package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadMovementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final HeadMovementSystem headMovementSystem = new HeadMovementSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(3, 3));
        dominion.createEntity(
            Head.INSTANCE,
            new Position(0, 0),
            new CurrentDirection(Direction.RIGHT)
        );
        headMovementSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Position.class,
                CurrentDirection.class
            )
        )
            .singleElement()
            .extracting(Results.With3::comp2, result -> result.comp3().value)
            .containsExactly(new Position(0, 0), Direction.RIGHT);
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    UP, 1, 1, 1, 2
                    DOWN, 1, 1, 1, 0
                    LEFT, 1, 1, 0, 1
                    RIGHT, 1, 1, 2, 1
                    UP, 2, 2, 2, 0
                    DOWN, 0, 0, 0, 2
                    LEFT, 0, 0, 2, 0
                    RIGHT, 2, 2, 0, 2
                    """
    )
    void givenTurnStartedEvent_thenHeadMoved(
        Direction direction,
        int initialX,
        int initialY,
        int expectedX,
        int expectedY
    ) {
        dominion.createEntity(new WorldDimensions(3, 3));
        dominion.createEntity(
            Head.INSTANCE,
            new Position(initialX, initialY),
            new CurrentDirection(direction)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        headMovementSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Position.class,
                CurrentDirection.class
            )
        )
            .singleElement()
            .extracting(Results.With3::comp2)
            .isEqualTo(new Position(expectedX, expectedY));
    }
}