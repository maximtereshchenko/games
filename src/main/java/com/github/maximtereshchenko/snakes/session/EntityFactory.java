package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.WorldEdit;

public final class EntityFactory {

    void createSegment(
        WorldEdit worldEdit,
        SegmentDefinition segmentDefinition,
        Position position
    ) {
        createEntity(
            worldEdit,
            new Segment(segmentDefinition.durationTurns),
            position,
            Colored.SEGMENT
        );
    }

    void createFoodEatenEvent(WorldEdit worldEdit) {
        createEvent(worldEdit, FoodEaten.INSTANCE);

    }

    void createFood(
        WorldEdit worldEdit,
        FoodDefinition foodDefinition,
        Position position
    ) {
        createEntity(
            worldEdit,
            Food.INSTANCE,
            new ForwardMovement(
                foodDefinition.periodTurns(),
                foodDefinition.periodTurns(),
                foodDefinition.direction()
            ),
            position,
            Colored.FOOD
        );
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
