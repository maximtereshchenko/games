package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.captor;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

final class EntityFactoryTest {

    private final World world = mock();
    private final EntityFactory entityFactory = new EntityFactory();
    private final ArgumentCaptor<Timer> timerCaptor = captor();

    @Test
    void whenCreateSegment_thenSegmentEntityCreated() {
        when(world.createEntity()).thenReturn(1);
        var position = new Position(2, 3);
        entityFactory.createSegment(world, position, 4);
        verify(world).createEntity();
        verify(world)
            .addComponents(
                eq(1),
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
        when(world.createEntity()).thenReturn(1);
        entityFactory.createFood(world, new Position(2, 3));
        verify(world).createEntity();
        verify(world)
            .addComponents(
                1,
                Food.INSTANCE,
                new Position(2, 3),
                Colored.FOOD
            );
    }

    @Test
    void whenCreateFoodEatenEvent_thenFoodEatenEventCreated() {
        when(world.createEntity()).thenReturn(1);
        entityFactory.createFoodEatenEvent(world);
        verify(world).createEntity();
        verify(world)
            .addComponents(
                1,
                FoodEaten.INSTANCE,
                Event.INSTANCE
            );
    }

    @Test
    void whenCreateTurnStartedEvent_thenTurnStartedEventCreated() {
        when(world.createEntity()).thenReturn(1);
        entityFactory.createTurnStartedEvent(world);
        verify(world).createEntity();
        verify(world)
            .addComponents(
                1,
                TurnStarted.INSTANCE,
                Event.INSTANCE
            );
    }
}
