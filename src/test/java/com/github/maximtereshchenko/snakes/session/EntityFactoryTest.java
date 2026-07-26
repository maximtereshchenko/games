package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.captor;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class EntityFactoryTest {

    private final Dominion dominion = mock();
    private final EntityFactory entityFactory = new EntityFactory();
    private final ArgumentCaptor<Timer> timerCaptor = captor();

    @Test
    void whenCreateSegment_thenSegmentEntityCreated() {
        var position = new Position(1, 2);
        entityFactory.createSegment(dominion, position, 4);
        verify(dominion)
            .createEntity(
                eq(Segment.INSTANCE),
                eq(position),
                timerCaptor.capture(),
                eq(Colored.SEGMENT)
            );
        assertThat(timerCaptor.getValue())
            .usingRecursiveComparison()
            .isEqualTo(new Timer(4, 4));
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
