package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodEatingSystemTest {

    private final Dominion dominion = Dominion.create();
    private final FoodEatingSystem foodEatingSystem = new FoodEatingSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Food.INSTANCE, new Position(0, 0));
        var before = dominion.findAllEntities().stream().toList();
        foodEatingSystem.run(0);
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenTurnStartedEvent_thenFoodEaten() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Food.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodEatingSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class, Position.class)).isEmpty();
        assertThat(dominion.findEntitiesWith(FoodEaten.class, Event.class)).hasSize(1);
    }

    @Test
    void givenHeadNotOnFood_thenNoFoodEaten() {
        dominion.createEntity(Head.INSTANCE, new Position(1, 0));
        dominion.createEntity(Food.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodEatingSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class, Position.class)).isNotEmpty();
        assertThat(dominion.findEntitiesWith(FoodEaten.class, Event.class)).isEmpty();
    }
}