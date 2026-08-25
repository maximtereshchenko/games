package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class FoodOpacitySynchronisationSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodEntities;

    FoodOpacitySynchronisationSystem(Registry registry) {
        super(registry);
        this.foodEntities = registry.view(
            new Query().all(Food.class, Opacity.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var foodEntity : foodEntities) {
            foodEntity.component(Opacity.class).value =
                foodEntity.component(Food.class).growth;
        }
    }
}
