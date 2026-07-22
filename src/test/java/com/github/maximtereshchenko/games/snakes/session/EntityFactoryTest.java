package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class EntityFactoryTest {

    private final Dominion dominion = mock();
    private final EntityFactory entityFactory = new EntityFactory();

    @Test
    void whenCreateSegment_thenSegmentEntityCreated() {
        entityFactory.createSegment(dominion, new Position(1, 2), 4);
        verify(dominion)
            .createEntity(
                Segment.INSTANCE,
                new Position(1, 2),
                new Timer(4),
                Colored.SEGMENT
            );
    }

    @Test
    void whenCreateFood_thenFoodEntityCreated() {
        entityFactory.createFood(dominion, new Position(5, 6));
        verify(dominion)
            .createEntity(Food.INSTANCE, new Position(5, 6), Colored.FOOD);
    }

    @Test
    void whenCreateFoodEatenEvent_thenFoodEatenEventCreated() {
        entityFactory.createFoodEatenEvent(dominion);
        verify(dominion).createEntity(FoodEaten.INSTANCE, Event.INSTANCE);
    }

    @Test
    void whenCreateTurnStartedEvent_thenTurnStartedEventCreated() {
        entityFactory.createTurnStartedEvent(dominion);
        verify(dominion).createEntity(TurnStarted.INSTANCE, Event.INSTANCE);
    }
}
