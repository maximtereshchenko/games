package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class SidewaysMovementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> movingEntities =
        world.entities(
            new Query()
                .all(
                    SidewaysMovement.class,
                    Direction.class,
                    WorldPositionIntent.class
                )
        );
    private final SidewaysMovementSystem sidewaysMovementSystem =
        new SidewaysMovementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(sidewaysMovementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new SidewaysMovement(1, 4, 1, 0),
            Direction.UP,
            new WorldPositionIntent(intent)
        );
        world.update(0);
        assertThat(movingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPositionIntent.class),
                entity -> entity.component(SidewaysMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new WorldPositionIntent(new WorldPosition(1, 1)),
                    new SidewaysMovement(1, 4, 1, 0)
                )
            );
    }

    @Test
    void givenRemainingTurnsNotZero_thenRemainingTurnsDecremented() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new SidewaysMovement(2, 4, 2, 0),
            Direction.UP,
            new WorldPositionIntent(intent)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(movingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPositionIntent.class),
                entity -> entity.component(SidewaysMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new WorldPositionIntent(new WorldPosition(1, 1)),
                    new SidewaysMovement(2, 4, 1, 0)
                )
            );
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
    void givenTurnStartedEvent_thenIntentMovedSideways(
        Direction direction,
        int initialX,
        int initialY,
        int cycle,
        int sidewaysIndex,
        int expectedX,
        int expectedY,
        int expectedSidewaysIndex
    ) {
        var intent = new WorldPosition(initialX, initialY);
        world.addComponents(
            world.createEntity(),
            new SidewaysMovement(1, cycle, 1, sidewaysIndex),
            direction,
            new WorldPositionIntent(intent)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(movingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPositionIntent.class),
                entity -> entity.component(SidewaysMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new WorldPositionIntent(new WorldPosition(expectedX, expectedY)),
                    new SidewaysMovement(1, cycle, 1, expectedSidewaysIndex)
                )
            );
    }
}
