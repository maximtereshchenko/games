package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class DirectedMovementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> movingEntities =
        registry.view(
            new Query()
                .all(
                    DirectedMovement.class,
                    Direction.class,
                    WorldPositionIntent.class
                )
        );
    private final DirectedMovementSystem directedMovementSystem =
        new DirectedMovementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(directedMovementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(0, 0);
        registry.addComponents(
            registry.createEntity(),
            new DirectedMovement(2, 1),
            Direction.RIGHT,
            new WorldPositionIntent(intent)
        );
        registry.update(0);
        assertThat(movingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPositionIntent.class),
                entity -> entity.component(DirectedMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new WorldPositionIntent(new WorldPosition(0, 0)),
                    new DirectedMovement(2, 1)
                )
            );
    }

    @Test
    void givenPositiveRemainingTurns_thenRemainingTurnsDecremented() {
        var intent = new WorldPosition(0, 0);
        registry.addComponents(
            registry.createEntity(),
            new DirectedMovement(2, 2),
            Direction.RIGHT,
            new WorldPositionIntent(intent)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(movingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPositionIntent.class),
                entity -> entity.component(DirectedMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new WorldPositionIntent(new WorldPosition(0, 0)),
                    new DirectedMovement(2, 1)
                )
            );
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
    void givenTurnStartedEvent_thenIntentMoved(
        Direction direction,
        int initialX,
        int initialY,
        int expectedX,
        int expectedY
    ) {
        var intent = new WorldPosition(initialX, initialY);
        registry.addComponents(
            registry.createEntity(),
            new DirectedMovement(2, 1),
            direction,
            new WorldPositionIntent(intent)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(movingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPositionIntent.class),
                entity -> entity.component(DirectedMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new WorldPositionIntent(new WorldPosition(expectedX, expectedY)),
                    new DirectedMovement(2, 2)
                )
            );
    }
}
