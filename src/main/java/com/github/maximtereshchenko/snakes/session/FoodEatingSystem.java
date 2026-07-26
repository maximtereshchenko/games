package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodEatingSystem extends TurnBasedSystem {

    private final Iterable<Entity> eatenFoodEntities;
    private final EntityFactory entityFactory;

    FoodEatingSystem(World world, EntityFactory entityFactory) {
        super(world);
        this.eatenFoodEntities = world.entities(
            new Query().all(Food.class, HeadCollisionTarget.class)
        );
        this.entityFactory = entityFactory;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var food : eatenFoodEntities) {
            worldEdit.deleteEntity(food.id());
            entityFactory.createFoodEatenEvent(worldEdit);
        }
    }
}
