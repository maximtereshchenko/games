package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.WorldEdit;

public final class EntityFactory {

    void createSegment(WorldEdit worldEdit, Position position, int turnsRemaining) {
        createEntity(
            worldEdit,
            Segment.INSTANCE,
            position,
            new Timer(turnsRemaining, turnsRemaining),
            Colored.SEGMENT
        );
    }

    void createFoodEatenEvent(WorldEdit worldEdit) {
        createEvent(worldEdit, FoodEaten.INSTANCE);

    }

    void createFood(WorldEdit worldEdit, Position position) {
        createEntity(worldEdit, Food.INSTANCE, position, Colored.FOOD);
    }

    void createTurnStartedEvent(WorldEdit worldEdit) {
        createEvent(worldEdit, TurnStarted.INSTANCE);
    }

    private void createEvent(WorldEdit worldEdit, Object tag) {
        createEntity(worldEdit, tag, Event.INSTANCE);
    }

    private void createEntity(WorldEdit worldEdit, Object... components) {
        worldEdit.addComponents(worldEdit.createEntity(), components);
    }
}
