package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirSupplyDecrementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> airSupplyEntities =
        world.entities(new Query().all(AirSupply.class));
    private final Iterable<Entity> deadEntities =
        world.entities(new Query().all(Dead.class));
    private final Iterable<Entity> deadAirSupplyEntities =
        world.entities(new Query().all(AirSupply.class, Dead.class));
    private final AirSupplyDecrementSystem airSupplyDecrementSystem =
        new AirSupplyDecrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(airSupplyDecrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new AirSupply(2, 1));
        world.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
        assertThat(deadEntities).isEmpty();
    }

    @Test
    void givenTurnStartedEvent_thenAirSupplyDecremented() {
        world.addComponents(world.createEntity(), new AirSupply(2, 2));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
        assertThat(deadEntities).isEmpty();
    }

    @Test
    void givenAirSupplyReachesZero_thenDeadAdded() {
        world.addComponents(world.createEntity(), new AirSupply(2, 1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(deadAirSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 0));
    }
}
