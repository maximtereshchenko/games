package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirSupplyResetSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> airSupplyEntities =
        registry.entities(new Query().all(AirSupply.class));
    private final AirSupplyResetSystem airSupplyResetSystem =
        new AirSupplyResetSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(airSupplyResetSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            new AirSupply(2, 1),
            new WorldPosition(0, 0)
        );
        registry.addComponents(
            registry.createEntity(),
            Air.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
    }

    @Test
    void givenNotOnAir_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            new AirSupply(2, 1),
            new WorldPosition(1, 1)
        );
        registry.addComponents(
            registry.createEntity(),
            Air.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
    }

    @Test
    void givenOnAir_thenAirSupplyReset() {
        registry.addComponents(
            registry.createEntity(),
            new AirSupply(2, 1),
            new WorldPosition(0, 0)
        );
        registry.addComponents(
            registry.createEntity(),
            Air.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 2));
    }
}
