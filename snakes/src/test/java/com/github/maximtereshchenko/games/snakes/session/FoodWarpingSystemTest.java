package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodWarpingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> foodWarpingEntities =
        registry.view(
            new Query()
                .all(
                    FoodWarping.class,
                    WorldPosition.class,
                    WorldPositionIntent.class
                )
        );
    private final FoodWarpingSystem foodWarpingSystem =
        new FoodWarpingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(foodWarpingSystem);
    }

    @Test
    void givenNoFoodConsumed_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            FoodWarping.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(new WorldPosition(0, 0))
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(3, 4)
        );
        registry.update(0);
        assertThat(foodWarpingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPosition.class),
                entity -> entity.component(WorldPositionIntent.class)
            )
            .containsExactly(
                new WorldPosition(0, 0),
                new WorldPositionIntent(new WorldPosition(0, 0))
            );
    }

    @Test
    void givenNoFood_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            new FoodConsumed(1),
            FoodWarping.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(new WorldPosition(0, 0))
        );
        registry.update(0);
        assertThat(foodWarpingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPosition.class),
                entity -> entity.component(WorldPositionIntent.class)
            )
            .containsExactly(
                new WorldPosition(0, 0),
                new WorldPositionIntent(new WorldPosition(0, 0))
            );
    }

    @Test
    void givenFoodConsumed_thenWarpedToFood() {
        registry.addComponents(
            registry.createEntity(),
            new FoodConsumed(1),
            FoodWarping.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(new WorldPosition(1, 1))
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(3, 4)
        );
        registry.update(0);
        assertThat(foodWarpingEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPosition.class),
                entity -> entity.component(WorldPositionIntent.class)
            )
            .containsExactly(
                new WorldPosition(3, 4),
                new WorldPositionIntent(new WorldPosition(3, 4))
            );
    }
}
