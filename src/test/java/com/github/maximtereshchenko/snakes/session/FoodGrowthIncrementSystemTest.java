package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodGrowthIncrementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodEntities =
        world.entities(new Query().all(Food.class));
    private final FoodGrowthIncrementSystem foodGrowthIncrementSystem =
        new FoodGrowthIncrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(foodGrowthIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new ConstantAmountFoodPolicy(1, Direction.RIGHT, 1, 0.3f)
        );
        world.addComponents(world.createEntity(), new Food(0.5f));
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Food.class).growth)
            .isEqualTo(0.5f);
    }

    @Test
    void givenNoFoodConsumed_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new ConstantAmountFoodPolicy(1, Direction.RIGHT, 1, 0.3f)
        );
        world.addComponents(world.createEntity(), new Food(0.5f));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Food.class).growth)
            .isEqualTo(0.5f);
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    0.3, 0.5, 0.8
                    0.6, 0.5, 1.0
                    0.2, 1.0, 1.0
                    """
    )
    void givenFoodConsumed_thenGrowthIncremented(
        float growthStep,
        float initialGrowth,
        float expectedGrowth
    ) {
        world.addComponents(
            world.createEntity(),
            new ConstantAmountFoodPolicy(1, Direction.RIGHT, 1, growthStep)
        );
        world.addComponents(world.createEntity(), new Food(initialGrowth));
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Food.class).growth)
            .isEqualTo(expectedGrowth);
    }
}
