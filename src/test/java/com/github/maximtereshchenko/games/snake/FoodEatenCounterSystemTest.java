package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodEatenCounterSystemTest {

    private final Dominion dominion = Dominion.create();
    private final FoodEatenCounterSystem foodEatenCounterSystem = new FoodEatenCounterSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(FoodEaten.INSTANCE);
        dominion.createEntity(new FoodEatenCounter(1));
        var before = dominion.findAllEntities().stream().toList();
        foodEatenCounterSystem.run(0);
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenFoodEaten_thenFoodEatenCounterIncremented() {
        dominion.createEntity(FoodEaten.INSTANCE);
        dominion.createEntity(new FoodEatenCounter(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodEatenCounterSystem.run(0);
        assertThat(dominion.findCompositionsWith(FoodEatenCounter.class))
            .singleElement()
            .extracting(result -> result.value)
            .isEqualTo(2);
    }

    @Test
    void givenNoFoodEaten_thenFoodEatenCounterNotIncremented() {
        dominion.createEntity(new FoodEatenCounter(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodEatenCounterSystem.run(0);
        assertThat(dominion.findCompositionsWith(FoodEatenCounter.class))
            .singleElement()
            .extracting(result -> result.value)
            .isEqualTo(1);
    }
}