package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class AirSupplyDecrementSystem extends TurnBasedSystem {

    private final Iterable<Entity> airSupplyEntities;

    AirSupplyDecrementSystem(World world) {
        super(world);
        this.airSupplyEntities = world.entities(
            new Query().all(AirSupply.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var airSupplyEntity : airSupplyEntities) {
            var airSupply = airSupplyEntity.component(AirSupply.class);
            airSupply.value--;
            if (airSupply.value == 0) {
                worldEdit.addComponents(airSupplyEntity.id(), Dead.INSTANCE);
            }
        }
    }
}
