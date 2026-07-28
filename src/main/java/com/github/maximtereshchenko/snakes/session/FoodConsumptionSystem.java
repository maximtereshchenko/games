package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class FoodConsumptionSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> foodEntities;

    FoodConsumptionSystem(World world) {
        super(world);
        this.headEntities = world.entities(
            new Query().all(Head.class, WorldPosition.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class, WorldPosition.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var headEntity : headEntities) {
            var headPosition = headEntity.component(WorldPosition.class);
            for (var foodEntity : foodEntities) {
                var foodPosition = foodEntity.component(WorldPosition.class);
                if (headPosition.equals(foodPosition)) {
                    worldEdit.deleteEntity(foodEntity.id());
                    worldEdit.addComponents(headEntity.id(), FoodConsumed.INSTANCE);
                }
            }
        }
    }
}
