package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class AirSupplyInterfaceElementSynchronisationSystem extends TurnBasedSystem {

    private final Iterable<Entity> airSupplyEntities;
    private final Iterable<Entity> airSupplyInterfaceElementEntities;
    private final I18NBundle bundle;

    AirSupplyInterfaceElementSynchronisationSystem(World world, I18NBundle bundle) {
        super(world);
        this.airSupplyEntities = world.entities(
            new Query().all(AirSupply.class)
        );
        this.airSupplyInterfaceElementEntities = world.entities(
            new Query().all(AirSupplyInterfaceElement.class, InterfaceText.class)
        );
        this.bundle = bundle;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var airSupplyEntity : airSupplyEntities) {
            for (var airSupplyInterfaceElementEntity : airSupplyInterfaceElementEntities) {
                airSupplyInterfaceElementEntity.component(InterfaceText.class).value =
                    bundle.get("screens.session.air.template")
                        .formatted(
                            airSupplyEntity.component(AirSupply.class).value
                        );
            }
        }
    }
}
