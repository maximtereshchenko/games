package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodGrowthIncrementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> foodEntities =
        registry.view(new Query().all(Food.class));
    private final FoodGrowthIncrementSystem foodGrowthIncrementSystem =
        new FoodGrowthIncrementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(foodGrowthIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            new ConstantAmountFoodPolicy(1, Direction.RIGHT, 1, 0.3f)
        );
        registry.addComponents(registry.createEntity(), new Food(0.5f));
        registry.addComponents(registry.createEntity(), new FoodConsumed(1));
        registry.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Food.class).growth)
            .isEqualTo(0.5f);
    }

    @Test
    void givenNoFoodConsumed_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            new ConstantAmountFoodPolicy(1, Direction.RIGHT, 1, 0.3f)
        );
        registry.addComponents(registry.createEntity(), new Food(0.5f));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
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
        registry.addComponents(
            registry.createEntity(),
            new ConstantAmountFoodPolicy(1, Direction.RIGHT, 1, growthStep)
        );
        registry.addComponents(registry.createEntity(), new Food(initialGrowth));
        registry.addComponents(registry.createEntity(), new FoodConsumed(1));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Food.class).growth)
            .isEqualTo(expectedGrowth);
    }
}
