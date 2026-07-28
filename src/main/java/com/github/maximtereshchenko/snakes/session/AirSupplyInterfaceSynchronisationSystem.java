package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;

import java.util.List;

final class AirSupplyInterfaceSynchronisationSystem
    extends LocalizableInterfaceTextSystem {

    private final Iterable<Entity> airSupplyEntities;

    AirSupplyInterfaceSynchronisationSystem(World world) {
        super(world, AirSupplyInterfaceElement.class);
        this.airSupplyEntities = world.entities(
            new Query().all(AirSupply.class)
        );
    }

    @Override
    void addVariables(List<Object> variables) {
        for (var airSupplyEntity : airSupplyEntities) {
            variables.add(airSupplyEntity.component(AirSupply.class).value);
        }
    }
}
