package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.mockito.Mockito.*;

final class FoodSpawningSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = mock();
    private final FoodSpawningSystem foodSpawningSystem =
        new FoodSpawningSystem(dominion, entityFactory, new Random(0), 2);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(2, 2));
        foodSpawningSystem.run(0);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnStartedEvent_thenFoodSpawned() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodSpawningSystem.run(0);
        verify(entityFactory).createFood(dominion, new Position(0, 1));
        verify(entityFactory).createFood(dominion, new Position(1, 1));
    }

    @Test
    void givenPosition_thenFoodSpawnedInFreeSpace() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(new Position(0, 1));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodSpawningSystem.run(0);
        verify(entityFactory).createFood(dominion, new Position(1, 0));
        verify(entityFactory).createFood(dominion, new Position(1, 1));
    }

    @Test
    void givenNotHasSpace_thenStopSpawningFood() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(new Position(0, 1));
        dominion.createEntity(new Position(1, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodSpawningSystem.run(0);
        verify(entityFactory).createFood(dominion, new Position(1, 1));
        verifyNoMoreInteractions(entityFactory);
    }

    @Test
    void givenBackground_thenFoodSpawned() {
        dominion.createEntity(new WorldDimensions(1, 1));
        dominion.createEntity(new Position(0, 0), Background.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        foodSpawningSystem.run(0);
        verify(entityFactory).createFood(dominion, new Position(0, 0));
        verifyNoMoreInteractions(entityFactory);
    }
}