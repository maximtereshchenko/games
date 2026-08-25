package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodRemovalSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> foodEntities =
        registry.view(new Query().all(Food.class));
    private final FoodRemovalSystem foodRemovalSystem =
        new FoodRemovalSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(foodRemovalSystem);
    }

    @Test
    void givenNoFoodConsumed_thenFoodNotRemoved() {
        registry.addComponents(registry.createEntity(), FoodWarping.INSTANCE);
        registry.addComponents(registry.createEntity(), new Food(1));
        registry.update(0);
        assertThat(foodEntities).hasSize(1);
    }

    @Test
    void givenNoFoodWarping_thenFoodNotRemoved() {
        registry.addComponents(registry.createEntity(), new FoodConsumed(1));
        registry.addComponents(registry.createEntity(), new Food(1));
        registry.update(0);
        assertThat(foodEntities).hasSize(1);
    }

    @Test
    void givenFoodConsumed_thenFoodRemoved() {
        registry.addComponents(
            registry.createEntity(),
            new FoodConsumed(1),
            FoodWarping.INSTANCE
        );
        registry.addComponents(registry.createEntity(), new Food(1));
        registry.addComponents(registry.createEntity(), new Food(1));
        registry.update(0);
        assertThat(foodEntities).isEmpty();
    }
}
