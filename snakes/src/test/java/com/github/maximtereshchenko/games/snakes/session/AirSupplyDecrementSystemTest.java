package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirSupplyDecrementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> airSupplyEntities =
        registry.view(new Query().all(AirSupply.class));
    private final Iterable<Entity> deadEntities =
        registry.view(new Query().all(Dead.class));
    private final Iterable<Entity> deadAirSupplyEntities =
        registry.view(new Query().all(AirSupply.class, Dead.class));
    private final AirSupplyDecrementSystem airSupplyDecrementSystem =
        new AirSupplyDecrementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(airSupplyDecrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(registry.createEntity(), new AirSupply(2, 1));
        registry.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
        assertThat(deadEntities).isEmpty();
    }

    @Test
    void givenTurnStartedEvent_thenAirSupplyDecremented() {
        registry.addComponents(registry.createEntity(), new AirSupply(2, 2));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
        assertThat(deadEntities).isEmpty();
    }

    @Test
    void givenAirSupplyReachesZero_thenDeadAdded() {
        registry.addComponents(registry.createEntity(), new AirSupply(2, 1));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(deadAirSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 0));
    }
}
