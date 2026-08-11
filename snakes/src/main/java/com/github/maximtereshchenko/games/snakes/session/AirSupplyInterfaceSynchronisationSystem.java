package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;

import java.util.List;

final class AirSupplyInterfaceSynchronisationSystem
    extends LocalizableInterfaceTextSystem {

    private final Iterable<Entity> airSupplyEntities;

    AirSupplyInterfaceSynchronisationSystem(Registry registry) {
        super(registry, AirSupplyInterfaceElement.class);
        this.airSupplyEntities = registry.entities(
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
