package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodCollisionSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodWorldPositionIntentEntities =
        world.entities(new Query().all(Food.class, WorldPositionIntent.class));
    private final FoodCollisionSystem foodCollisionSystem =
        new FoodCollisionSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(foodCollisionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new Food(1),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.addComponents(
            world.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(1, 1)
        );
        world.update(0);
        assertThat(foodWorldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class).value)
            .isEqualTo(new WorldPosition(1, 1));
    }

    @Test
    void givenFoodIntentOnWall_thenIntentReverted() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new Food(1),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.addComponents(
            world.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(1, 1)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodWorldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class).value)
            .isEqualTo(new WorldPosition(0, 0));
    }

    @Test
    void givenFoodIntentNotOnWall_thenIntentUnchanged() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new Food(1),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.addComponents(
            world.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(2, 2)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodWorldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class).value)
            .isEqualTo(new WorldPosition(1, 1));
    }
}
