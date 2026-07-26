package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AirCounterDecrementSystemTest {

    private final World world = new World();
    private final AirCounterDecrementSystem airCounterDecrementSystem =
        new AirCounterDecrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(airCounterDecrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new AirCounter(2, 1));
        world.update(0);
        assertThat(world.entities(new Query().all(AirCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(AirCounter.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 1));
    }

    @Test
    void givenTurnStartedEvent_thenAirCounterDecremented() {
        world.addComponents(world.createEntity(), new AirCounter(2, 1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(AirCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(AirCounter.class))
            .usingRecursiveComparison()
            .isEqualTo(new AirCounter(2, 0));
    }
}