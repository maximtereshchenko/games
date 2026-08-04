package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

final class WarpingEdgeSpawningSystemTest {

    private final World world = new World();
    private final Iterable<Entity> warpingEdgeEntities =
        world.entities(new Query().all(WarpingEdge.class));
    private final Iterable<Entity> spawnedWarpingEdgeEntities =
        world.entities(
            new Query()
                .all(
                    WarpingEdge.class,
                    WorldPosition.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final Iterable<Entity> warpingPolicyEntities =
        world.entities(new Query().all(WarpingPolicy.class));
    private final WarpingEdgeSpawningSystem warpingEdgeSpawningSystem =
        new WarpingEdgeSpawningSystem(world);

    private static Stream<Arguments> warpingEdges() {
        return Stream.of(
            arguments(
                0,
                Map.ofEntries(
                    entry(new WorldPosition(0, 6), WarpingEdge.TOP),
                    entry(new WorldPosition(1, 6), WarpingEdge.TOP),
                    entry(new WorldPosition(2, 6), WarpingEdge.TOP),
                    entry(new WorldPosition(3, 6), WarpingEdge.TOP),
                    entry(new WorldPosition(4, 6), WarpingEdge.TOP),
                    entry(new WorldPosition(5, 6), WarpingEdge.TOP),
                    entry(new WorldPosition(6, 6), WarpingEdge.TOP),
                    entry(new WorldPosition(0, 0), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(1, 0), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(2, 0), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(3, 0), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(4, 0), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(5, 0), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(6, 0), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(0, 1), WarpingEdge.LEFT),
                    entry(new WorldPosition(0, 2), WarpingEdge.LEFT),
                    entry(new WorldPosition(0, 3), WarpingEdge.LEFT),
                    entry(new WorldPosition(0, 4), WarpingEdge.LEFT),
                    entry(new WorldPosition(0, 5), WarpingEdge.LEFT),
                    entry(new WorldPosition(6, 1), WarpingEdge.RIGHT),
                    entry(new WorldPosition(6, 2), WarpingEdge.RIGHT),
                    entry(new WorldPosition(6, 3), WarpingEdge.RIGHT),
                    entry(new WorldPosition(6, 4), WarpingEdge.RIGHT),
                    entry(new WorldPosition(6, 5), WarpingEdge.RIGHT)
                )
            ),
            arguments(
                1,
                Map.ofEntries(
                    entry(new WorldPosition(1, 5), WarpingEdge.TOP),
                    entry(new WorldPosition(2, 5), WarpingEdge.TOP),
                    entry(new WorldPosition(3, 5), WarpingEdge.TOP),
                    entry(new WorldPosition(4, 5), WarpingEdge.TOP),
                    entry(new WorldPosition(5, 5), WarpingEdge.TOP),
                    entry(new WorldPosition(1, 1), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(2, 1), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(3, 1), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(4, 1), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(5, 1), WarpingEdge.BOTTOM),
                    entry(new WorldPosition(1, 2), WarpingEdge.LEFT),
                    entry(new WorldPosition(1, 3), WarpingEdge.LEFT),
                    entry(new WorldPosition(1, 4), WarpingEdge.LEFT),
                    entry(new WorldPosition(5, 2), WarpingEdge.RIGHT),
                    entry(new WorldPosition(5, 3), WarpingEdge.RIGHT),
                    entry(new WorldPosition(5, 4), WarpingEdge.RIGHT)
                )
            ),
            arguments(
                2,
                Map.of(
                    new WorldPosition(2, 4), WarpingEdge.TOP,
                    new WorldPosition(3, 4), WarpingEdge.TOP,
                    new WorldPosition(4, 4), WarpingEdge.TOP,
                    new WorldPosition(2, 2), WarpingEdge.BOTTOM,
                    new WorldPosition(3, 2), WarpingEdge.BOTTOM,
                    new WorldPosition(4, 2), WarpingEdge.BOTTOM,
                    new WorldPosition(2, 3), WarpingEdge.LEFT,
                    new WorldPosition(4, 3), WarpingEdge.RIGHT
                )
            )
        );
    }

    @BeforeEach
    void setUp() {
        world.addSystems(warpingEdgeSpawningSystem);
    }

    @Test
    void givenNoFoodConsumedAndNotInitializing_thenNoWarpingEdges() {
        world.addComponents(world.createEntity(), new WarpingPolicy(1, 1, 0));
        world.addComponents(world.createEntity(), new WorldDimensions(7, 7));
        world.update(0);
        assertThat(warpingEdgeEntities).isEmpty();
    }

    @Test
    void givenFoodConsumedButPeriodNotReached_thenNoWarpingEdges() {
        world.addComponents(world.createEntity(), new WarpingPolicy(2, 2, 0));
        world.addComponents(world.createEntity(), new WorldDimensions(7, 7));
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.update(0);
        assertThat(warpingEdgeEntities).isEmpty();
        assertThat(warpingPolicyEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WarpingPolicy.class).remainingConsumedFood,
                entity -> entity.component(WarpingPolicy.class).layers
            )
            .containsExactly(1, 0);
    }

    @ParameterizedTest
    @MethodSource("warpingEdges")
    void givenFoodConsumedAndPeriodReached_thenWarpingEdgesSpawned(
        int layers,
        Map<WorldPosition, WarpingEdge> expectedWarpingEdges
    ) {
        world.addComponents(world.createEntity(), new WarpingPolicy(1, 1, layers));
        world.addComponents(world.createEntity(), new WorldDimensions(7, 7));
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.update(0);
        assertThat(spawnedWarpingEdgeEntities).hasSize(expectedWarpingEdges.size());
        for (var entity : spawnedWarpingEdgeEntities) {
            assertThat(entity.component(PaletteColor.class)).isEqualTo(PaletteColor.WARP);
            assertThat(entity.component(Opacity.class)).isEqualTo(new Opacity(1));
            assertThat(expectedWarpingEdges)
                .containsEntry(
                    entity.component(WorldPosition.class),
                    entity.component(WarpingEdge.class)
                );
        }
    }

    @ParameterizedTest
    @MethodSource("warpingEdges")
    void givenInitializing_thenWarpingEdgesSpawned(
        int layers,
        Map<WorldPosition, WarpingEdge> expectedWarpingEdges
    ) {
        world.addComponents(
            world.createEntity(),
            new WarpingPolicy(1, 1, layers),
            Initializing.INSTANCE
        );
        world.addComponents(world.createEntity(), new WorldDimensions(7, 7));
        world.update(0);
        assertThat(spawnedWarpingEdgeEntities).hasSize(expectedWarpingEdges.size());
        for (var entity : spawnedWarpingEdgeEntities) {
            assertThat(entity.component(PaletteColor.class)).isEqualTo(PaletteColor.WARP);
            assertThat(entity.component(Opacity.class)).isEqualTo(new Opacity(1));
            assertThat(expectedWarpingEdges)
                .containsEntry(
                    entity.component(WorldPosition.class),
                    entity.component(WarpingEdge.class)
                );
        }
    }
}
