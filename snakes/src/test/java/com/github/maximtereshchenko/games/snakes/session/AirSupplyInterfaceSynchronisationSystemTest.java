package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class AirSupplyInterfaceSynchronisationSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> localizableInterfaceTextEntities =
        registry.view(
            new Query().all(AirSupplyInterfaceElement.class, LocalizableInterfaceText.class)
        );
    private final AirSupplyInterfaceSynchronisationSystem system =
        new AirSupplyInterfaceSynchronisationSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(system);
    }

    @Test
    void whenUpdated_thenAirSupplyAddedToVariables() {
        registry.addComponents(registry.createEntity(), new AirSupply(5, 3));
        registry.addComponents(
            registry.createEntity(),
            AirSupplyInterfaceElement.INSTANCE,
            new LocalizableInterfaceText("screens.session.air.template", new ArrayList<>())
        );
        registry.update(0);
        assertThat(localizableInterfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(LocalizableInterfaceText.class).variables())
            .isEqualTo(List.of(3));
    }
}
