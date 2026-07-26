package com.github.maximtereshchenko.snakes.session;

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
        dominion.createEntity(Food.INSTANCE, HeadCollisionTarget.INSTANCE);
        foodEatingSystem.run(0);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnStartedEvent_thenFoodEaten() {
        dominion.createEntity(Food.INSTANCE, HeadCollisionTarget.INSTANCE);
        dominion.createEntity(TurnStarted.INSTANCE);
        foodEatingSystem.run(0);
        assertThat(dominion.findEntitiesWith(Food.class)).isEmpty();
        verify(entityFactory).createFoodEatenEvent(dominion);
    }
}