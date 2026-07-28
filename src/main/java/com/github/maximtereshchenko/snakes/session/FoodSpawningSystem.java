package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

final class FoodSpawningSystem extends TurnBasedSystem {

    private final Iterable<Entity> nonBackgroundEntities;
    private final Iterable<Entity> foodDefinitionEntities;
    private final Iterable<Entity> foodEntities;
    private final Random random;

    FoodSpawningSystem(World world, Random random) {
        super(world);
        this.nonBackgroundEntities = world.entities(
            new Query()
                .all(WorldPosition.class)
                .none(Background.class)
        );
        this.foodDefinitionEntities = world.entities(
            new Query().all(FoodDefinition.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
        this.random = random;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        if (foodEntities.iterator().hasNext()) {
            return;
        }
        for (var foodDefinitionEntity : foodDefinitionEntities) {
            var foodDefinition = foodDefinitionEntity.component(FoodDefinition.class);
            var worldPosition = position(foodDefinition.worldDimensions());
            var worldPositionIntent = new WorldPosition();
            worldPositionIntent.copy(worldPosition);
            worldEdit.addComponents(
                worldEdit.createEntity(),
                Food.INSTANCE,
                new DirectedMovement(
                    foodDefinition.periodTurns(),
                    foodDefinition.periodTurns()
                ),
                worldPosition,
                new WorldPositionIntent(worldPositionIntent),
                foodDefinition.direction(),
                Colored.FOOD
            );
        }
    }

    private Set<WorldPosition> worldPositions() {
        var worldPositions = new HashSet<WorldPosition>();
        for (var entity : nonBackgroundEntities) {
            worldPositions.add(entity.component(WorldPosition.class));
        }
        return worldPositions;
    }

    private WorldPosition position(WorldDimensions worldDimensions) {
        var worldPositions = worldPositions();
        var worldPosition = new WorldPosition();
        do {
            worldPosition.x = random.nextInt(worldDimensions.width());
            worldPosition.y = random.nextInt(worldDimensions.height());
        } while (worldPositions.contains(worldPosition));
        return worldPosition;
    }
}
