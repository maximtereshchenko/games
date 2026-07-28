package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodCollisionSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodEntities;
    private final Iterable<Entity> wallEntities;

    FoodCollisionSystem(World world) {
        super(world);
        this.foodEntities = world.entities(
            new Query().all(Food.class, WorldPosition.class, WorldPositionIntent.class)
        );
        this.wallEntities = world.entities(
            new Query().all(WorldPosition.class, Wall.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var foodEntity : foodEntities) {
            var foodWorldPositionIntent = foodEntity.component(WorldPositionIntent.class);
            for (var wallEntity : wallEntities) {
                var wallWorldPosition = wallEntity.component(WorldPosition.class);
                if (foodWorldPositionIntent.value.equals(wallWorldPosition)) {
                    foodWorldPositionIntent.value.copy(foodEntity.component(WorldPosition.class));
                }
            }
        }
    }
}
