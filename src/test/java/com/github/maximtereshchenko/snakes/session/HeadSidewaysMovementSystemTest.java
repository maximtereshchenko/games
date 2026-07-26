package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadSidewaysMovementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final HeadSidewaysMovementSystem headSidewaysMovementSystem =
        new HeadSidewaysMovementSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(3, 3));
        dominion.createEntity(
            Head.INSTANCE,
            new Timer(0, 0),
            new SidewaysMovement(4, 0),
            new Position(1, 1),
            new CurrentForwardDirection(Direction.UP)
        );
        headSidewaysMovementSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Timer.class,
                SidewaysMovement.class,
                Position.class,
                CurrentForwardDirection.class
            )
        )
            .singleElement()
            .extracting(
                Results.With5::comp4,
                result -> result.comp3().index
            )
            .containsExactly(new Position(1, 1), 0);
    }

    @Test
    void givenTimerNotExpired_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(3, 3));
        dominion.createEntity(
            Head.INSTANCE,
            new Timer(1, 1),
            new SidewaysMovement(4, 0),
            new Position(1, 1),
            new CurrentForwardDirection(Direction.UP)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        headSidewaysMovementSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Timer.class,
                SidewaysMovement.class,
                Position.class,
                CurrentForwardDirection.class
            )
        )
            .singleElement()
            .extracting(
                Results.With5::comp4,
                result -> result.comp3().index
            )
            .containsExactly(new Position(1, 1), 0);
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    UP, 1, 1, 4, 0, 2, 1, 1
                    RIGHT, 1, 1, 4, 0, 1, 0, 1
                    UP, 1, 1, 4, 2, 0, 1, 3
                    UP, 1, 1, 3, 2, 0, 1, 0
                    """
    )
    void givenTurnStartedEvent_thenHeadMovedSideways(
        Direction direction,
        int initialX,
        int initialY,
        int cycle,
        int sidewaysIndex,
        int expectedX,
        int expectedY,
        int expectedSidewaysIndex
    ) {
        dominion.createEntity(new WorldDimensions(3, 3));
        dominion.createEntity(
            Head.INSTANCE,
            new Timer(0, 0),
            new SidewaysMovement(cycle, sidewaysIndex),
            new Position(initialX, initialY),
            new CurrentForwardDirection(direction)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        headSidewaysMovementSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Timer.class,
                SidewaysMovement.class,
                Position.class,
                CurrentForwardDirection.class
            )
        )
            .singleElement()
            .extracting(
                Results.With5::comp4,
                result -> result.comp3().index
            )
            .containsExactly(new Position(expectedX, expectedY), expectedSidewaysIndex);
    }
}
