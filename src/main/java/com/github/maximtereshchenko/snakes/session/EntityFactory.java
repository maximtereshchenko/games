package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;

public final class EntityFactory {

    void createSegment(Dominion dominion, Position position, int turnsRemaining) {
        dominion.createEntity(
            Segment.INSTANCE,
            position,
            new Timer(turnsRemaining, turnsRemaining),
            Colored.SEGMENT
        );
    }

    void createFoodEatenEvent(Dominion dominion) {
        createEvent(dominion, FoodEaten.INSTANCE);

    }

    void createFood(Dominion dominion, Position position) {
        dominion.createEntity(Food.INSTANCE, position, Colored.FOOD);
    }

    void createTurnStartedEvent(Dominion dominion) {
        createEvent(dominion, TurnStarted.INSTANCE);
    }

    private void createEvent(Dominion dominion, Object tag) {
        dominion.createEntity(tag, Event.INSTANCE);
    }
}
