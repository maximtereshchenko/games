package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class AirSupplyInterfaceSynchronisationSystemTest {

    private final World world = new World();
    private final Iterable<Entity> localizableInterfaceTextEntities =
        world.entities(
            new Query().all(AirSupplyInterfaceElement.class, LocalizableInterfaceText.class)
        );
    private final AirSupplyInterfaceSynchronisationSystem system =
        new AirSupplyInterfaceSynchronisationSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(system);
    }

    @Test
    void whenUpdated_thenAirSupplyAddedToVariables() {
        world.addComponents(world.createEntity(), new AirSupply(5, 3));
        world.addComponents(
            world.createEntity(),
            AirSupplyInterfaceElement.INSTANCE,
            new LocalizableInterfaceText("screens.session.air.template", new ArrayList<>())
        );
        world.update(0);
        assertThat(localizableInterfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(LocalizableInterfaceText.class).variables())
            .isEqualTo(List.of(3));
    }
}
