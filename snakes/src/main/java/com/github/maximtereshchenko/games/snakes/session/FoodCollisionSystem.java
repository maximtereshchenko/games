package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class FoodCollisionSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodEntities;
    private final Iterable<Entity> wallEntities;

    FoodCollisionSystem(Registry registry) {
        super(registry);
        this.foodEntities = registry.entities(
            new Query().all(Food.class, WorldPosition.class, WorldPositionIntent.class)
        );
        this.wallEntities = registry.entities(
            new Query().all(WorldPosition.class, Wall.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var foodEntity : foodEntities) {
            var foodWorldPositionIntent = foodEntity.component(WorldPositionIntent.class);
            for (var wallEntity : wallEntities) {
                var wallWorldPosition = wallEntity.component(WorldPosition.class);
                if (foodWorldPositionIntent.value().equals(wallWorldPosition)) {
                    foodWorldPositionIntent.value().copy(foodEntity.component(WorldPosition.class));
                }
            }
        }
    }
}
