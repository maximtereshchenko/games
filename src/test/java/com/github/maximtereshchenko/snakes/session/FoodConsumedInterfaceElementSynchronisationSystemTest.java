package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodConsumedInterfaceElementSynchronisationSystemTest {

    private final World world = new World();
    private final Iterable<Entity> interfaceTextEntities =
        world.entities(
            new Query().all(FoodConsumedInterfaceElement.class, InterfaceText.class)
        );
    private final FoodConsumedInterfaceElementSynchronisationSystem system =
        new FoodConsumedInterfaceElementSynchronisationSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(system);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var statistics = new Statistics();
        statistics.value.put(SessionMetric.FOOD_CONSUMED, 4);
        world.addComponents(
            world.createEntity(),
            statistics,
            FoodConsumed.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            FoodConsumedInterfaceElement.INSTANCE,
            new InterfaceText(1, "0")
        );
        world.update(0);
        assertThat(interfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfaceText.class).value)
            .isEqualTo("0");
    }

    @Test
    void givenFoodConsumed_thenInterfaceTextSynchronised() {
        var statistics = new Statistics();
        statistics.value.put(SessionMetric.FOOD_CONSUMED, 4);
        world.addComponents(
            world.createEntity(),
            statistics,
            FoodConsumed.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            FoodConsumedInterfaceElement.INSTANCE,
            new InterfaceText(1, "0")
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(interfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfaceText.class).value)
            .isEqualTo("4");
    }
}
