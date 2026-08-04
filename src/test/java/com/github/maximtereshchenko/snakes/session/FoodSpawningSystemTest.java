package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodSpawningSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodEntities =
        world.entities(new Query().all(Food.class));
    private final Iterable<Entity> spawnedFoodEntities =
        world.entities(
            new Query()
                .all(
                    Food.class,
                    DirectedMovement.class,
                    WorldPosition.class,
                    WorldPositionIntent.class,
                    Direction.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final Iterable<Entity> foodWithPositionEntities =
        world.entities(new Query().all(Food.class, WorldPosition.class));
    private final FoodSpawningSystem foodSpawningSystem =
        new FoodSpawningSystem(world, new Random(0));

    @BeforeEach
    void setUp() {
        world.addSystems(foodSpawningSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(2, 2)
        );
        world.update(0);
        assertThat(foodEntities).isEmpty();
    }

    @Test
    void givenInitializing_thenFoodSpawned() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 1),
            Initializing.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(2, 2)
        );
        world.update(0);
        assertThat(spawnedFoodEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(DirectedMovement.class),
                entity -> entity.component(Direction.class),
                entity -> entity.component(PaletteColor.class),
                entity -> entity.component(Opacity.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new DirectedMovement(1, 1),
                    Direction.RIGHT,
                    PaletteColor.FOOD,
                    new Opacity(1)
                )
            );
    }

    @Test
    void givenTurnStartedEvent_thenFoodSpawned() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(2, 2)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(spawnedFoodEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(DirectedMovement.class),
                entity -> entity.component(Direction.class),
                entity -> entity.component(PaletteColor.class),
                entity -> entity.component(Opacity.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new DirectedMovement(1, 1),
                    Direction.RIGHT,
                    PaletteColor.FOOD,
                    new Opacity(1)
                )
            );
    }

    @Test
    void givenEnoughFood_thenNoFoodSpawned() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(2, 2)
        );
        world.addComponents(
            world.createEntity(),
            Food.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities).hasSize(1);
    }

    @Test
    void givenSomeFood_thenRemainingFoodSpawned() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 5)
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(2, 2)
        );
        world.addComponents(
            world.createEntity(),
            Food.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities)
            .extracting(entity -> entity.component(WorldPosition.class))
            .containsExactlyInAnyOrder(
                new WorldPosition(0, 0),
                new WorldPosition(1, 1),
                new WorldPosition(0, 1),
                new WorldPosition(1, 0)
            );
    }

    @Test
    void givenOccupiedPosition_thenFoodSpawnedInFreeSpace() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(1, 2)
        );
        world.addComponents(world.createEntity(), new WorldPosition(0, 0));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodWithPositionEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class))
            .isEqualTo(new WorldPosition(0, 1));
    }

    @Test
    void givenBackground_thenFoodSpawnedOnBackground() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(1, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldPosition(0, 0),
            Background.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodWithPositionEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class))
            .isEqualTo(new WorldPosition(0, 0));
    }

    @Test
    void givenNotEnoughSpace_thenNoFoodSpawned() {
        world.addComponents(
            world.createEntity(),
            new FoodPolicy(1, Direction.RIGHT, 2)
        );
        world.addComponents(
            world.createEntity(),
            new WorldDimensions(1, 1)
        );
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities).isEmpty();
    }
}
