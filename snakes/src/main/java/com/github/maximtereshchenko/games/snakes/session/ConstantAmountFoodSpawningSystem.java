package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

final class ConstantAmountFoodSpawningSystem implements System {

    private final Iterable<Entity> turnStartedEntities;
    private final Iterable<Entity> initializingEntities;
    private final Iterable<Entity> constantAmountFoodPolicyEntities;
    private final Iterable<Entity> nonBackgroundEntities;
    private final View foodEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Random random;

    ConstantAmountFoodSpawningSystem(Registry registry, Random random) {
        this.turnStartedEntities = registry.view(
            new Query().all(TurnStarted.class)
        );
        this.initializingEntities = registry.view(
            new Query().all(ConstantAmountFoodPolicy.class, Initializing.class)
        );
        this.constantAmountFoodPolicyEntities = registry.view(
            new Query().all(ConstantAmountFoodPolicy.class)
        );
        this.nonBackgroundEntities = registry.view(
            new Query()
                .all(WorldPosition.class)
                .none(Background.class)
        );
        this.foodEntities = registry.view(
            new Query().all(Food.class)
        );
        this.worldDimensionsEntities = registry.view(
            new Query().all(WorldDimensions.class)
        );
        this.random = random;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        spawnFood(registryEdit, initializingEntities);
        if (!turnStartedEntities.iterator().hasNext()) {
            return;
        }
        spawnFood(registryEdit, constantAmountFoodPolicyEntities);
    }

    private void spawnFood(RegistryEdit registryEdit, Iterable<Entity> entities) {
        for (var entity : entities) {
            var constantAmountFoodPolicy = entity.component(ConstantAmountFoodPolicy.class);
            for (var worldDimensionsEntity : worldDimensionsEntities) {
                var worldDimensions = worldDimensionsEntity.component(WorldDimensions.class);
                var worldPositions = worldPositions();
                for (
                    var index = foodEntities.size();
                    index < constantAmountFoodPolicy.max() &&
                    worldPositions.size() < space(worldDimensions);
                    index++
                ) {
                    var worldPosition = new WorldPosition();
                    do {
                        worldPosition.x = random.nextInt(worldDimensions.width());
                        worldPosition.y = random.nextInt(worldDimensions.height());
                    } while (!worldPositions.add(worldPosition));
                    spawnFood(registryEdit, worldPosition, constantAmountFoodPolicy, index);
                }
            }
        }
    }

    private int space(WorldDimensions worldDimensions) {
        return worldDimensions.height() * worldDimensions.width();
    }

    private void spawnFood(
        RegistryEdit registryEdit,
        WorldPosition worldPosition,
        ConstantAmountFoodPolicy constantAmountFoodPolicy,
        int index
    ) {
        var worldPositionIntent = new WorldPosition();
        worldPositionIntent.copy(worldPosition);
        registryEdit.addComponents(
            registryEdit.createEntity(),
            new Food(1.0f - index * constantAmountFoodPolicy.growthStep()),
            new DirectedMovement(
                constantAmountFoodPolicy.periodTurns(),
                constantAmountFoodPolicy.periodTurns()
            ),
            worldPosition,
            new WorldPositionIntent(worldPositionIntent),
            constantAmountFoodPolicy.direction(),
            PaletteColor.FOOD,
            new Opacity(1)
        );
    }

    private Set<WorldPosition> worldPositions() {
        var worldPositions = new HashSet<WorldPosition>();
        for (var entity : nonBackgroundEntities) {
            worldPositions.add(entity.component(WorldPosition.class));
        }
        return worldPositions;
    }
}
