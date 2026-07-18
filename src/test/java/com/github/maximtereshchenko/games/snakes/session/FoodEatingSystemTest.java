package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class FoodEatingSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = mock();
    private final FoodEatingSystem foodEatingSystem =
        new FoodEatingSystem(dominion, entityFactory);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Food.INSTANCE, new Position(0, 0));
        foodEatingSystem.run(0);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnStartedEvent_thenFoodEaten() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Food.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodEatingSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class, Position.class)).isEmpty();
        verify(entityFactory).createFoodEatenEvent(dominion);
    }

    @Test
    void givenHeadNotOnFood_thenNoFoodEaten() {
        dominion.createEntity(Head.INSTANCE, new Position(1, 0));
        dominion.createEntity(Food.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        foodEatingSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class, Position.class)).isNotEmpty();
        verifyNoInteractions(entityFactory);
    }
}