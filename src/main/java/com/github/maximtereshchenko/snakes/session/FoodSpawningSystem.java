package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

final class FoodSpawningSystem implements System {

    private final Iterable<Entity> turnStartedEntities;
    private final Iterable<Entity> initializingEntities;
    private final Iterable<Entity> foodPolicyEntities;
    private final Iterable<Entity> nonBackgroundEntities;
    private final Iterable<Entity> foodEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Random random;

    FoodSpawningSystem(World world, Random random) {
        this.turnStartedEntities = world.entities(
            new Query().all(TurnStarted.class)
        );
        this.initializingEntities = world.entities(
            new Query().all(FoodPolicy.class, Initializing.class)
        );
        this.foodPolicyEntities = world.entities(
            new Query().all(FoodPolicy.class)
        );
        this.nonBackgroundEntities = world.entities(
            new Query()
                .all(WorldPosition.class)
                .none(Background.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
        this.worldDimensionsEntities = world.entities(
            new Query().all(WorldDimensions.class)
        );
        this.random = random;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        spawnFood(worldEdit, initializingEntities);
        if (!turnStartedEntities.iterator().hasNext()) {
            return;
        }
        spawnFood(worldEdit, foodPolicyEntities);
    }

    private void spawnFood(WorldEdit worldEdit, Iterable<Entity> entities) {
        for (var foodPolicyEntity : entities) {
            var foodPolicy = foodPolicyEntity.component(FoodPolicy.class);
            for (var worldDimensionsEntity : worldDimensionsEntities) {
                var worldDimensions = worldDimensionsEntity.component(WorldDimensions.class);
                var worldPositions = worldPositions();
                for (
                    var i = food();
                    i < foodPolicy.max() &&
                    worldPositions.size() < space(worldDimensions);
                    i++
                ) {
                    var worldPosition = new WorldPosition();
                    do {
                        worldPosition.x = random.nextInt(worldDimensions.width());
                        worldPosition.y = random.nextInt(worldDimensions.height());
                    } while (!worldPositions.add(worldPosition));
                    spawnFood(worldEdit, worldPosition, foodPolicy);
                }
            }
        }
    }

    private int space(WorldDimensions worldDimensions) {
        return worldDimensions.height() * worldDimensions.width();
    }

    private void spawnFood(
        WorldEdit worldEdit,
        WorldPosition worldPosition,
        FoodPolicy foodPolicy
    ) {
        var worldPositionIntent = new WorldPosition();
        worldPositionIntent.copy(worldPosition);
        worldEdit.addComponents(
            worldEdit.createEntity(),
            Food.INSTANCE,
            new DirectedMovement(
                foodPolicy.periodTurns(),
                foodPolicy.periodTurns()
            ),
            worldPosition,
            new WorldPositionIntent(worldPositionIntent),
            foodPolicy.direction(),
            Colored.FOOD
        );
    }

    private Set<WorldPosition> worldPositions() {
        var worldPositions = new HashSet<WorldPosition>();
        for (var entity : nonBackgroundEntities) {
            worldPositions.add(entity.component(WorldPosition.class));
        }
        return worldPositions;
    }

    private int food() {
        var food = 0;
        for (var _ : foodEntities) {
            food++;
        }
        return food;
    }
}
