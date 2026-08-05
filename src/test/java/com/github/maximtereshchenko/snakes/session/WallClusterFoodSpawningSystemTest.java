package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

final class WallClusterFoodSpawningSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodEntities =
        world.entities(new Query().all(Food.class));
    private final Iterable<Entity> wallEntities =
        world.entities(new Query().all(Wall.class));
    private final Iterable<Entity> spawnedFoodEntities =
        world.entities(
            new Query()
                .all(
                    Food.class,
                    WorldPosition.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final Iterable<Entity> spawnedWallEntities =
        world.entities(
            new Query()
                .all(
                    Wall.class,
                    WorldPosition.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final WallClusterFoodSpawningSystem wallClusterFoodSpawningSystem =
        new WallClusterFoodSpawningSystem(world, new Random(0));

    @BeforeEach
    void setUp() {
        world.addSystems(wallClusterFoodSpawningSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            WallClusterFoodPolicy.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(5, 5)
        );
        world.update(0);
        assertThat(foodEntities).isEmpty();
        assertThat(wallEntities).isEmpty();
    }

    @Test
    void givenNoWallClusterFoodPolicy_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(5, 5)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities).isEmpty();
        assertThat(wallEntities).isEmpty();
    }

    @Test
    void givenInitializing_thenClusterSpawned() {
        world.addComponents(
            world.createEntity(),
            WallClusterFoodPolicy.INSTANCE,
            Initializing.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(5, 5)
        );
        world.update(0);
        assertCluster(
            new WorldPosition(1, 2),
            new WorldPosition(0, 1),
            new WorldPosition(1, 1),
            new WorldPosition(2, 1),
            new WorldPosition(0, 2),
            new WorldPosition(2, 2),
            new WorldPosition(0, 3),
            new WorldPosition(1, 3),
            new WorldPosition(2, 3)
        );
    }

    @Test
    void givenTurnStartedEvent_thenClusterSpawned() {
        world.addComponents(
            world.createEntity(),
            WallClusterFoodPolicy.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(5, 5)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertCluster(
            new WorldPosition(1, 2),
            new WorldPosition(0, 1),
            new WorldPosition(1, 1),
            new WorldPosition(2, 1),
            new WorldPosition(0, 2),
            new WorldPosition(2, 2),
            new WorldPosition(0, 3),
            new WorldPosition(1, 3),
            new WorldPosition(2, 3)
        );
    }

    @Test
    void givenExistingFood_thenNoClusterSpawned() {
        world.addComponents(
            world.createEntity(),
            WallClusterFoodPolicy.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(5, 5)
        );
        world.addComponents(
            world.createEntity(),
            new Food(1),
            new WorldPosition(0, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities).hasSize(1);
        assertThat(wallEntities).isEmpty();
    }

    @Test
    void givenOccupiedPosition_thenClusterSpawnedInFreeSpace() {
        world.addComponents(
            world.createEntity(),
            WallClusterFoodPolicy.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(5, 5)
        );
        world.addComponents(world.createEntity(), new WorldPosition(1, 2));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertCluster(
            new WorldPosition(3, 3),
            new WorldPosition(2, 2),
            new WorldPosition(3, 2),
            new WorldPosition(4, 2),
            new WorldPosition(2, 3),
            new WorldPosition(4, 3),
            new WorldPosition(2, 4),
            new WorldPosition(3, 4),
            new WorldPosition(4, 4)
        );
    }

    @Test
    void givenBackground_thenClusterSpawnedOnBackground() {
        world.addComponents(
            world.createEntity(),
            WallClusterFoodPolicy.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(5, 5)
        );
        world.addComponents(
            world.createEntity(),
            new WorldPosition(1, 2),
            Background.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertCluster(
            new WorldPosition(1, 2),
            new WorldPosition(0, 1),
            new WorldPosition(1, 1),
            new WorldPosition(2, 1),
            new WorldPosition(0, 2),
            new WorldPosition(2, 2),
            new WorldPosition(0, 3),
            new WorldPosition(1, 3),
            new WorldPosition(2, 3)
        );
    }

    @Test
    void givenNotEnoughSpace_thenNoClusterSpawned() {
        world.addComponents(
            world.createEntity(),
            WallClusterFoodPolicy.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(3, 3)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities).isEmpty();
        assertThat(wallEntities).isEmpty();
    }

    private void assertCluster(WorldPosition wall, WorldPosition... food) {
        assertThat(spawnedFoodEntities).hasSize(8);
        assertThat(spawnedWallEntities).hasSize(1);
        assertThat(spawnedFoodEntities)
            .extracting(entity -> entity.component(Food.class))
            .usingRecursiveFieldByFieldElementComparator()
            .containsOnly(new Food(1));
        assertThat(spawnedFoodEntities)
            .extracting(entity -> entity.component(PaletteColor.class))
            .containsOnly(PaletteColor.FOOD);
        assertThat(spawnedFoodEntities)
            .extracting(entity -> entity.component(Opacity.class))
            .usingRecursiveFieldByFieldElementComparator()
            .containsOnly(new Opacity(1));
        assertThat(spawnedFoodEntities)
            .extracting(entity -> entity.component(WorldPosition.class))
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(food);
        assertThat(spawnedWallEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(PaletteColor.class),
                entity -> entity.component(Opacity.class),
                entity -> entity.component(WorldPosition.class)
            )
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactly(
                PaletteColor.WALL,
                new Opacity(1),
                wall
            );
    }
}
