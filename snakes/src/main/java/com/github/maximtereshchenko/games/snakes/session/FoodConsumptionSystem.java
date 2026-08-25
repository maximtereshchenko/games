package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class FoodConsumptionSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> foodEntities;

    FoodConsumptionSystem(Registry registry) {
        super(registry);
        this.headEntities = registry.view(
            new Query().all(Head.class, WorldPosition.class, Hitbox.class)
        );
        this.foodEntities = registry.view(
            new Query().all(Food.class, WorldPosition.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var headEntity : headEntities) {
            var headPosition = headEntity.component(WorldPosition.class);
            var hitboxRadius = headEntity.component(Hitbox.class).radius();
            var foodConsumed = 0;
            for (var foodEntity : foodEntities) {
                var growth = foodEntity.component(Food.class).growth;
                var foodPosition = foodEntity.component(WorldPosition.class);
                if (isOne(growth) && touched(headPosition, foodPosition, hitboxRadius)) {
                    registryEdit.deleteEntity(foodEntity.id());
                    foodConsumed++;
                }
            }
            if (foodConsumed != 0) {
                registryEdit.addComponents(headEntity.id(), new FoodConsumed(foodConsumed));
            }
        }
    }

    private boolean isOne(float growth) {
        return 1f - growth < 0.001f;
    }

    private boolean touched(
        WorldPosition head,
        WorldPosition food,
        int radius
    ) {
        return touched(head.x, food.x, radius) &&
               touched(head.y, food.y, radius);
    }

    private boolean touched(
        int head,
        int food,
        int radius
    ) {
        return Math.abs(head - food) <= radius;
    }
}
