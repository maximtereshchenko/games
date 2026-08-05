package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodWarpingSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodWarpingEntities =
        world.entities(
            new Query()
                .all(
                    FoodWarping.class,
                    WorldPosition.class,
                    WorldPositionIntent.class
                )
        );
    private final FoodWarpingSystem foodWarpingSystem =
        new FoodWarpingSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(foodWarpingSystem);
    }

    @Test
    void givenNoFoodConsumed_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            FoodWarping.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(new WorldPosition(0, 0))
        );
        world.addComponents(
            world.createEntity(),
            new Food(1),
            new WorldPosition(3, 4)
        );
        world.update(0);
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
        world.addComponents(
            world.createEntity(),
            new FoodConsumed(1),
            FoodWarping.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(new WorldPosition(0, 0))
        );
        world.update(0);
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
        world.addComponents(
            world.createEntity(),
            new FoodConsumed(1),
            FoodWarping.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(new WorldPosition(1, 1))
        );
        world.addComponents(
            world.createEntity(),
            new Food(1),
            new WorldPosition(3, 4)
        );
        world.update(0);
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
