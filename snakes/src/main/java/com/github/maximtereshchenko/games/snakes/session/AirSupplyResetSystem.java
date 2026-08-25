package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class AirSupplyResetSystem extends TurnBasedSystem {

    private final Iterable<Entity> airSupplyEntities;
    private final Iterable<Entity> airEntities;

    AirSupplyResetSystem(Registry registry) {
        super(registry);
        this.airSupplyEntities = registry.view(
            new Query().all(WorldPosition.class, AirSupply.class)
        );
        this.airEntities = registry.view(
            new Query().all(Air.class, WorldPosition.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var airSupplyEntity : airSupplyEntities) {
            var worldPosition = airSupplyEntity.component(WorldPosition.class);
            for (var airEntity : airEntities) {
                var airWorldPosition = airEntity.component(WorldPosition.class);
                if (worldPosition.equals(airWorldPosition)) {
                    var airSupply = airSupplyEntity.component(AirSupply.class);
                    airSupply.value = airSupply.capacity;
                }
            }
        }
    }
}
