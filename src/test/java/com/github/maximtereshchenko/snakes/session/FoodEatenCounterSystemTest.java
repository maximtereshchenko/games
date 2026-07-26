package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodEatenCounterSystemTest {

    private final World world = new World();
    private final FoodEatenCounterSystem foodEatenCounterSystem = new FoodEatenCounterSystem(
        world
    );

    @BeforeEach
    void setUp() {
        world.addSystems(foodEatenCounterSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), FoodEaten.INSTANCE);
        world.addComponents(world.createEntity(), new FoodEatenCounter(1));
        world.update(0);
        assertThat(world.entities(new Query().all(FoodEatenCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(FoodEatenCounter.class).value)
            .isEqualTo(1);
    }

    @Test
    void givenFoodEaten_thenFoodEatenCounterIncremented() {
        world.addComponents(world.createEntity(), FoodEaten.INSTANCE);
        world.addComponents(world.createEntity(), new FoodEatenCounter(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(FoodEatenCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(FoodEatenCounter.class).value)
            .isEqualTo(2);
    }

    @Test
    void givenNoFoodEaten_thenFoodEatenCounterNotIncremented() {
        world.addComponents(world.createEntity(), new FoodEatenCounter(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(FoodEatenCounter.class)))
            .singleElement()
            .extracting(entity -> entity.component(FoodEatenCounter.class).value)
            .isEqualTo(1);
    }
}