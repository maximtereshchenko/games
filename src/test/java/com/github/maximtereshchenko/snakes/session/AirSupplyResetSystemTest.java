package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirSupplyResetSystemTest {

    private final World world = new World();
    private final Iterable<Entity> airSupplyEntities =
        world.entities(new Query().all(AirSupply.class));
    private final AirSupplyResetSystem airSupplyResetSystem =
        new AirSupplyResetSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(airSupplyResetSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new AirSupply(2, 1),
            new WorldPosition(0, 0)
        );
        world.addComponents(
            world.createEntity(),
            Air.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
    }

    @Test
    void givenNotOnAir_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new AirSupply(2, 1),
            new WorldPosition(1, 1)
        );
        world.addComponents(
            world.createEntity(),
            Air.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 1));
    }

    @Test
    void givenOnAir_thenAirSupplyReset() {
        world.addComponents(
            world.createEntity(),
            new AirSupply(2, 1),
            new WorldPosition(0, 0)
        );
        world.addComponents(
            world.createEntity(),
            Air.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(airSupplyEntities)
            .singleElement()
            .extracting(entity -> entity.component(AirSupply.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirSupply(2, 2));
    }
}
