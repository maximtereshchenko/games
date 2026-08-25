package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class AirSupplyDecrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> airSupplyEntities;

    AirSupplyDecrementSystem(Registry registry) {
        super(registry);
        this.airSupplyEntities = registry.view(
            new Query().all(AirSupply.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var airSupplyEntity : airSupplyEntities) {
            var airSupply = airSupplyEntity.component(AirSupply.class);
            airSupply.value--;
            if (airSupply.value == 0) {
                registryEdit.addComponents(airSupplyEntity.id(), Dead.INSTANCE);
            }
        }
    }
}
