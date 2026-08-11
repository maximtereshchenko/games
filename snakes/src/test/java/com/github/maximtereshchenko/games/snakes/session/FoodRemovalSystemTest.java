package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodRemovalSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodEntities =
        world.entities(new Query().all(Food.class));
    private final FoodRemovalSystem foodRemovalSystem =
        new FoodRemovalSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(foodRemovalSystem);
    }

    @Test
    void givenNoFoodConsumed_thenFoodNotRemoved() {
        world.addComponents(world.createEntity(), FoodWarping.INSTANCE);
        world.addComponents(world.createEntity(), new Food(1));
        world.update(0);
        assertThat(foodEntities).hasSize(1);
    }

    @Test
    void givenNoFoodWarping_thenFoodNotRemoved() {
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.addComponents(world.createEntity(), new Food(1));
        world.update(0);
        assertThat(foodEntities).hasSize(1);
    }

    @Test
    void givenFoodConsumed_thenFoodRemoved() {
        world.addComponents(
            world.createEntity(),
            new FoodConsumed(1),
            FoodWarping.INSTANCE
        );
        world.addComponents(world.createEntity(), new Food(1));
        world.addComponents(world.createEntity(), new Food(1));
        world.update(0);
        assertThat(foodEntities).isEmpty();
    }
}
