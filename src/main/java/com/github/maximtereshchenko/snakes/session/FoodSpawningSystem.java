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
    private final Iterable<Entity> foodPolicyEntities;
    private final Iterable<Entity> foodEntities;
    private final Random random;

    FoodSpawningSystem(World world, Random random) {
        super(world);
        this.nonBackgroundEntities = world.entities(
            new Query()
                .all(WorldPosition.class)
                .none(Background.class)
        );
        this.foodPolicyEntities = world.entities(
            new Query().all(FoodPolicy.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
        this.random = random;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var foodPolicyEntity : foodPolicyEntities) {
            var foodPolicy = foodPolicyEntity.component(FoodPolicy.class);
            var worldDimensions = foodPolicy.worldDimensions();
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
                } while (worldPositions.contains(worldPosition));
                createFood(worldEdit, worldPosition, foodPolicy);
            }
        }
    }

    private int space(WorldDimensions worldDimensions) {
        return worldDimensions.height() * worldDimensions.width();
    }

    private void createFood(
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
