package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodConsumptionSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> foodEntities =
        registry.view(new Query().all(Food.class));
    private final Iterable<Entity> foodConsumedEntities =
        registry.view(new Query().all(FoodConsumed.class));
    private final Iterable<Entity> headFoodConsumedEntities =
        registry.view(new Query().all(Head.class, FoodConsumed.class));
    private final FoodConsumptionSystem foodConsumptionSystem =
        new FoodConsumptionSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(foodConsumptionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new Hitbox(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(0, 0)
        );
        registry.update(0);
        assertThat(foodEntities).hasSize(1);
        assertThat(foodConsumedEntities).isEmpty();
    }

    @Test
    void givenHeadOnFood_thenFoodConsumed() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new Hitbox(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities).isEmpty();
        assertThat(headFoodConsumedEntities)
            .singleElement()
            .extracting(entity -> entity.component(FoodConsumed.class))
            .isEqualTo(new FoodConsumed(1));
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    0, 2
                    1, 2
                    2, 2
                    0, 1
                    1, 1
                    2, 1
                    0, 2
                    1, 2
                    2, 2
                    """
    )
    void givenWideHitbox_thenFoodConsumed(int x, int y) {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(1, 1),
            new Hitbox(1)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(x, y)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities).isEmpty();
        assertThat(headFoodConsumedEntities)
            .singleElement()
            .extracting(entity -> entity.component(FoodConsumed.class))
            .isEqualTo(new FoodConsumed(1));
    }

    @Test
    void givenHeadNotOnFood_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new Hitbox(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(1, 1)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities).hasSize(1);
        assertThat(foodConsumedEntities).isEmpty();
    }

    @Test
    void givenFoodGrowthNotOne_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(1, 1),
            new Hitbox(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(0.9f),
            new WorldPosition(1, 1)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities).hasSize(1);
        assertThat(foodConsumedEntities).isEmpty();
    }

    @Test
    void givenFoodGrowthAlmostOne_thenFoodConsumed() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new Hitbox(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(0.9999f),
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities).isEmpty();
        assertThat(headFoodConsumedEntities)
            .singleElement()
            .extracting(entity -> entity.component(FoodConsumed.class))
            .isEqualTo(new FoodConsumed(1));
    }

    @Test
    void givenHeadOnManyFood_thenManyFoodConsumed() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new Hitbox(1)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(0, 1)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(1, 0)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities).isEmpty();
        assertThat(headFoodConsumedEntities)
            .singleElement()
            .extracting(entity -> entity.component(FoodConsumed.class))
            .isEqualTo(new FoodConsumed(2));
    }
}
