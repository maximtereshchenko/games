package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadForwardMovementSystemTest {

    private final World world = new World();
    private final HeadForwardMovementSystem headForwardMovementSystem =
        new HeadForwardMovementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(headForwardMovementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new WorldDimensions(3, 3));
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Position(0, 0),
            new CurrentForwardDirection(Direction.RIGHT)
        );
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Position.class,
                        CurrentForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(CurrentForwardDirection.class).value
            )
            .containsExactly(new Position(0, 0), Direction.RIGHT);
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    UP, 1, 1, 1, 2
                    DOWN, 1, 1, 1, 0
                    LEFT, 1, 1, 0, 1
                    RIGHT, 1, 1, 2, 1
                    """
    )
    void givenTurnStartedEvent_thenHeadMoved(
        Direction direction,
        int initialX,
        int initialY,
        int expectedX,
        int expectedY
    ) {
        world.addComponents(world.createEntity(), new WorldDimensions(3, 3));
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
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
                        Position.class,
                        CurrentForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(entity -> entity.component(Position.class))
            .isEqualTo(new Position(expectedX, expectedY));
    }
}