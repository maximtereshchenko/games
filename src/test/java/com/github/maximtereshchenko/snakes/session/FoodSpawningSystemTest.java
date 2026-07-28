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
                    Colored.class
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
            new FoodDefinition(new WorldDimensions(2, 2), 1, Direction.RIGHT)
        );
        world.update(0);
        assertThat(foodEntities).isEmpty();
    }

    @Test
    void givenTurnStartedEvent_thenFoodSpawned() {
        world.addComponents(
            world.createEntity(),
            new FoodDefinition(new WorldDimensions(2, 2), 1, Direction.RIGHT)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(spawnedFoodEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(DirectedMovement.class),
                entity -> entity.component(Direction.class),
                entity -> entity.component(Colored.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new DirectedMovement(1, 1),
                    Direction.RIGHT,
                    Colored.FOOD
                )
            );
    }

    @Test
    void givenSomeFood_thenNoFoodSpawned() {
        world.addComponents(
            world.createEntity(),
            new FoodDefinition(new WorldDimensions(2, 2), 1, Direction.RIGHT)
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
    void givenOccupiedPosition_thenFoodSpawnedInFreeSpace() {
        world.addComponents(
            world.createEntity(),
            new FoodDefinition(new WorldDimensions(1, 2), 1, Direction.RIGHT)
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
            new FoodDefinition(new WorldDimensions(1, 1), 1, Direction.RIGHT)
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
}
