package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodSpawningSystemTest {

    private final Dominion dominion = Dominion.create();
    private final FoodSpawningSystem foodSpawningSystem = new FoodSpawningSystem(
        dominion,
        new Random(0),
        2
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(2, 2));
        var before = dominion.findAllEntities().stream().toList();
        foodSpawningSystem.run(0);
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenTurnStartedEvent_thenFoodSpawned() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodSpawningSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class, Position.class, Colored.class))
            .allSatisfy(result -> assertThat(result.comp3()).isEqualTo(Colored.FOOD))
            .extracting(Results.With3::comp2)
            .containsExactlyInAnyOrder(new Position(0, 1), new Position(1, 1));
    }

    @Test
    void givenPosition_thenFoodSpawnedInFreeSpace() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(new Position(0, 1));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodSpawningSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class, Position.class, Colored.class))
            .allSatisfy(result -> assertThat(result.comp3()).isEqualTo(Colored.FOOD))
            .extracting(Results.With3::comp2)
            .containsExactlyInAnyOrder(new Position(1, 0), new Position(1, 1));
    }

    @Test
    void givenNotHasSpace_thenStopSpawningFood() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(new Position(0, 1));
        dominion.createEntity(new Position(1, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodSpawningSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class, Position.class, Colored.class))
            .singleElement()
            .extracting(Results.With3::comp2, Results.With3::comp3)
            .containsExactlyInAnyOrder(new Position(1, 1), Colored.FOOD);
    }
}