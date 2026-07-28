package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class AirSupplyResetSystem extends TurnBasedSystem {

    private final Iterable<Entity> airSupplyEntities;
    private final Iterable<Entity> airEntities;

    AirSupplyResetSystem(World world) {
        super(world);
        this.airSupplyEntities = world.entities(
            new Query().all(WorldPosition.class, AirSupply.class)
        );
        this.airEntities = world.entities(
            new Query().all(Air.class, WorldPosition.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
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
