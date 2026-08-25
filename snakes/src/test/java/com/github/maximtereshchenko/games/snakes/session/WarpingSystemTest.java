package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> warpedEntities = registry.view(
        new Query().all(Warped.class, WorldPositionIntent.class)
    );
    private final WarpingSystem warpingSystem = new WarpingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(warpingSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            WarpingEdge.RIGHT,
            new WorldPosition(1, 1)
        );
        registry.addComponents(
            registry.createEntity(),
            new WorldPositionIntent(new WorldPosition(1, 1)),
            WarpingDestinationEdge.OPPOSITE
        );
        registry.addComponents(registry.createEntity(), new WarpingPolicy(1, 1, 1));
        registry.addComponents(registry.createEntity(), new WorldDimensions(4, 4));
        registry.update(0);
        assertThat(warpedEntities).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    RIGHT, 9, 2, 1, OPPOSITE, 1, 2
                    LEFT, 0, 2, 1, OPPOSITE, 8, 2
                    TOP, 2, 5, 1, OPPOSITE, 2, 1
                    BOTTOM, 2, 0, 1, OPPOSITE, 2, 4
                    RIGHT, 9, 1, 1, CLOCKWISE, 1, 1
                    RIGHT, 9, 2, 1, CLOCKWISE, 3, 1
                    RIGHT, 9, 4, 1, CLOCKWISE, 8, 1
                    LEFT, 0, 1, 1, CLOCKWISE, 1, 4
                    LEFT, 0, 2, 1, CLOCKWISE, 3, 4
                    LEFT, 0, 4, 1, CLOCKWISE, 8, 4
                    TOP, 1, 5, 1, CLOCKWISE, 8, 4
                    TOP, 4, 5, 1, CLOCKWISE, 8, 3
                    TOP, 8, 5, 1, CLOCKWISE, 8, 1
                    BOTTOM, 1, 0, 1, CLOCKWISE, 1, 4
                    BOTTOM, 4, 0, 1, CLOCKWISE, 1, 3
                    BOTTOM, 8, 0, 1, CLOCKWISE, 1, 1
                    RIGHT, 9, 1, 1, COUNTER_CLOCKWISE, 8, 4
                    RIGHT, 9, 2, 1, COUNTER_CLOCKWISE, 6, 4
                    RIGHT, 9, 4, 1, COUNTER_CLOCKWISE, 1, 4
                    LEFT, 0, 1, 1, COUNTER_CLOCKWISE, 8, 1
                    LEFT, 0, 2, 1, COUNTER_CLOCKWISE, 6, 1
                    LEFT, 0, 4, 1, COUNTER_CLOCKWISE, 1, 1
                    TOP, 1, 5, 1, COUNTER_CLOCKWISE, 1, 1
                    TOP, 4, 5, 1, COUNTER_CLOCKWISE, 1, 2
                    TOP, 8, 5, 1, COUNTER_CLOCKWISE, 1, 4
                    BOTTOM, 1, 0, 1, COUNTER_CLOCKWISE, 8, 1
                    BOTTOM, 4, 0, 1, COUNTER_CLOCKWISE, 8, 2
                    BOTTOM, 8, 0, 1, COUNTER_CLOCKWISE, 8, 4
                    RIGHT, 8, 2, 2, OPPOSITE, 2, 2
                    RIGHT, 8, 3, 2, OPPOSITE, 2, 3
                    LEFT, 1, 2, 2, OPPOSITE, 7, 2
                    LEFT, 1, 3, 2, OPPOSITE, 7, 3
                    TOP, 2, 4, 2, OPPOSITE, 2, 2
                    TOP, 4, 4, 2, OPPOSITE, 4, 2
                    TOP, 7, 4, 2, OPPOSITE, 7, 2
                    BOTTOM, 2, 0, 2, OPPOSITE, 2, 3
                    BOTTOM, 4, 0, 2, OPPOSITE, 4, 3
                    BOTTOM, 7, 0, 2, OPPOSITE, 7, 3
                    RIGHT, 8, 2, 2, CLOCKWISE, 2, 2
                    RIGHT, 8, 3, 2, CLOCKWISE, 7, 2
                    LEFT, 1, 2, 2, CLOCKWISE, 2, 3
                    LEFT, 1, 3, 2, CLOCKWISE, 7, 3
                    TOP, 2, 4, 2, CLOCKWISE, 7, 3
                    TOP, 7, 4, 2, CLOCKWISE, 7, 2
                    BOTTOM, 2, 1, 2, CLOCKWISE, 2, 3
                    BOTTOM, 7, 1, 2, CLOCKWISE, 2, 2
                    RIGHT, 8, 2, 2, COUNTER_CLOCKWISE, 7, 3
                    RIGHT, 8, 3, 2, COUNTER_CLOCKWISE, 2, 3
                    LEFT, 1, 2, 2, COUNTER_CLOCKWISE, 7, 2
                    LEFT, 1, 3, 2, COUNTER_CLOCKWISE, 2, 2
                    TOP, 2, 4, 2, COUNTER_CLOCKWISE, 2, 2
                    TOP, 7, 4, 2, COUNTER_CLOCKWISE, 2, 3
                    BOTTOM, 2, 1, 2, COUNTER_CLOCKWISE, 7, 2
                    BOTTOM, 7, 1, 2, COUNTER_CLOCKWISE, 7, 3
                    """
    )
    void givenWarpingEdgeCollision_thenEntityWarped(
        WarpingEdge warpingEdge,
        int warpingEdgeX,
        int warpingEdgeY,
        int layers,
        WarpingDestinationEdge warpingDestinationEdge,
        int expectedX,
        int expectedY
    ) {
        registry.addComponents(
            registry.createEntity(),
            warpingEdge,
            new WorldPosition(warpingEdgeX, warpingEdgeY)
        );
        registry.addComponents(
            registry.createEntity(),
            new WorldPositionIntent(new WorldPosition(warpingEdgeX, warpingEdgeY)),
            warpingDestinationEdge
        );
        registry.addComponents(registry.createEntity(), new WarpingPolicy(1, 1, layers));
        registry.addComponents(registry.createEntity(), new WorldDimensions(10, 6));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(warpedEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class))
            .usingRecursiveComparison()
            .isEqualTo(
                new WorldPositionIntent(
                    new WorldPosition(expectedX, expectedY)
                )
            );
    }
}