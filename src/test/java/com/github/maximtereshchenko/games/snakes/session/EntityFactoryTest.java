package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.configuration.Configuration;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

final class EntityFactoryTest {

    private final Configuration configuration = mock();
    private final Dominion dominion = mock();
    private final Mode mode = mock();
    private final EntityFactory entityFactory = new EntityFactory(configuration);

    @Test
    void whenCreateWorld_thenEntitiesCreated() {
        when(configuration.snakeLength()).thenReturn(3);
        var worldDimensions = new WorldDimensions(3, 4);
        entityFactory.createWorld(dominion, worldDimensions);
        verify(dominion).createEntity(any(Session.class));
        verify(dominion).createEntity(worldDimensions);
        verify(dominion).createEntity(any(TurnTimer.class));
        verify(dominion).createEntity(new InitialSegmentTimer(3));
        verify(dominion).createEntity(any(SessionStatisticsAccumulator.class));
        verify(dominion)
            .createEntity(
                any(FoodEatenCounter.class),
                eq(Colored.FOOD_EATEN_COUNTER)
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(0, 0),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(1, 0),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(2, 0),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(0, 1),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(1, 1),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(2, 1),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(0, 2),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(1, 2),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(2, 2),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(0, 3),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(1, 3),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Background.INSTANCE,
                new Position(2, 3),
                Colored.BACKGROUND
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(0, 0),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(1, 0),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(2, 0),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(0, 3),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(1, 3),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(2, 3),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(0, 3),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(0, 2),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(0, 1),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(0, 0),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(2, 3),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(2, 2),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(2, 1),
                Colored.WARP
            );
        verify(dominion)
            .createEntity(
                Warp.INSTANCE,
                new Position(2, 0),
                Colored.WARP
            );
        verifyNoMoreInteractions(dominion);
    }

    @Test
    void whenCreateHead_thenHeadEntityCreated() {
        when(configuration.snakeHeadPosition()).thenReturn(new Position(1, 2));
        when(configuration.snakeHeadForwardDirection()).thenReturn(Direction.LEFT);
        when(mode.headMovementSidewaysCycle()).thenReturn(3);
        when(mode.headMovementSidewaysInterval()).thenReturn(4);
        entityFactory.createHead(dominion, mode);
        verify(dominion)
            .createEntity(
                Head.INSTANCE,
                new CurrentForwardDirection(Direction.LEFT),
                new NextForwardDirection(Direction.LEFT),
                new SidewaysDirection(3),
                new Position(1, 2),
                new Timer(4),
                Colored.HEAD
            );
    }

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
