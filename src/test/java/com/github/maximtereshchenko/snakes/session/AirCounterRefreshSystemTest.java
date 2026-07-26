package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirCounterRefreshSystemTest {

    private final World world = new World();
    private final AirCounterRefreshSystem airCounterRefreshSystem =
        new AirCounterRefreshSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(airCounterRefreshSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new AirCounter(2, 1));
        world.addComponents(world.createEntity(), Head.INSTANCE, new Position(0, 0));
        world.addComponents(world.createEntity(), Air.INSTANCE, new Position(0, 0));
        world.update(0);
        assertThat(world.entities(new Query().all(AirCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(AirCounter.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 1));
    }

    @Test
    void givenNoHeadOnAir_thenNoChanges() {
        world.addComponents(world.createEntity(), new AirCounter(2, 1));
        world.addComponents(world.createEntity(), Head.INSTANCE, new Position(1, 1));
        world.addComponents(world.createEntity(), Air.INSTANCE, new Position(0, 0));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(AirCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(AirCounter.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 1));
    }

    @Test
    void givenHeadOnAir_thenAirCounterRefreshed() {
        world.addComponents(world.createEntity(), new AirCounter(2, 1));
        world.addComponents(world.createEntity(), Head.INSTANCE, new Position(0, 0));
        world.addComponents(world.createEntity(), Air.INSTANCE, new Position(0, 0));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(AirCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(AirCounter.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 2));
    }
}