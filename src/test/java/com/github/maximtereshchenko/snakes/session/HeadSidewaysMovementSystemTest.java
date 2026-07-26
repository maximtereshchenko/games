package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadSidewaysMovementSystemTest {

    private final World world = new World();
    private final HeadSidewaysMovementSystem headSidewaysMovementSystem =
        new HeadSidewaysMovementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(headSidewaysMovementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new WorldDimensions(3, 3));
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Timer(0, 0),
            new SidewaysMovement(4, 0),
            new Position(1, 1),
            new CurrentForwardDirection(Direction.UP)
        );
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Timer.class,
                        SidewaysMovement.class,
                        Position.class,
                        CurrentForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(SidewaysMovement.class).index
            )
            .containsExactly(new Position(1, 1), 0);
    }

    @Test
    void givenTimerNotExpired_thenNoChanges() {
        world.addComponents(world.createEntity(), new WorldDimensions(3, 3));
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Timer(1, 1),
            new SidewaysMovement(4, 0),
            new Position(1, 1),
            new CurrentForwardDirection(Direction.UP)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Timer.class,
                        SidewaysMovement.class,
                        Position.class,
                        CurrentForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(SidewaysMovement.class).index
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
        world.addComponents(world.createEntity(), new WorldDimensions(3, 3));
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Timer(0, 0),
            new SidewaysMovement(cycle, sidewaysIndex),
            new Position(initialX, initialY),
            new CurrentForwardDirection(direction)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Timer.class,
                        SidewaysMovement.class,
                        Position.class,
                        CurrentForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(SidewaysMovement.class).index
            )
            .containsExactly(
                new Position(expectedX, expectedY),
                expectedSidewaysIndex
            );
    }
}
