package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.mockito.Mockito.*;

final class FoodSpawningSystemTest {

    private final World world = new World();
    private final EntityFactory entityFactory = mock();
    private final FoodSpawningSystem foodSpawningSystem =
        new FoodSpawningSystem(world, entityFactory, new Random(0), 2);

    @BeforeEach
    void setUp() {
        world.addSystems(foodSpawningSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new WorldDimensions(2, 2));
        world.update(0);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnStartedEvent_thenFoodSpawned() {
        world.addComponents(world.createEntity(), new WorldDimensions(2, 2));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        verify(entityFactory)
            .createFood(
                any(WorldEdit.class),
                eq(new Position(0, 1))
            );
        verify(entityFactory)
            .createFood(
                any(WorldEdit.class),
                eq(new Position(1, 1))
            );
    }

    @Test
    void givenPosition_thenFoodSpawnedInFreeSpace() {
        world.addComponents(world.createEntity(), new WorldDimensions(2, 2));
        world.addComponents(world.createEntity(), new Position(0, 1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        verify(entityFactory)
            .createFood(
                any(WorldEdit.class),
                eq(new Position(1, 0))
            );
        verify(entityFactory)
            .createFood(
                any(WorldEdit.class),
                eq(new Position(1, 1))
            );
    }

    @Test
    void givenNotHasSpace_thenStopSpawningFood() {
        world.addComponents(world.createEntity(), new WorldDimensions(2, 2));
        world.addComponents(world.createEntity(), new Position(0, 0));
        world.addComponents(world.createEntity(), new Position(0, 1));
        world.addComponents(world.createEntity(), new Position(1, 0));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        verify(entityFactory)
            .createFood(
                any(WorldEdit.class),
                eq(new Position(1, 1))
            );
        verifyNoMoreInteractions(entityFactory);
    }

    @Test
    void givenBackground_thenFoodSpawned() {
        world.addComponents(world.createEntity(), new WorldDimensions(1, 1));
        world.addComponents(
            world.createEntity(),
            new Position(0, 0),
            Background.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        verify(entityFactory)
            .createFood(
                any(WorldEdit.class),
                eq(new Position(0, 0))
            );
        verifyNoMoreInteractions(entityFactory);
    }
}