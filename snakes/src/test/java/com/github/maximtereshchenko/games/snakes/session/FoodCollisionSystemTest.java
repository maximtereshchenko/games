package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodCollisionSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> foodWorldPositionIntentEntities =
        registry.view(new Query().all(Food.class, WorldPositionIntent.class));
    private final FoodCollisionSystem foodCollisionSystem =
        new FoodCollisionSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(foodCollisionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(1, 1);
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        registry.addComponents(
            registry.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(1, 1)
        );
        registry.update(0);
        assertThat(foodWorldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class))
            .isEqualTo(new WorldPositionIntent(new WorldPosition(1, 1)));
    }

    @Test
    void givenFoodIntentOnWall_thenIntentReverted() {
        var intent = new WorldPosition(1, 1);
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        registry.addComponents(
            registry.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(1, 1)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodWorldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class))
            .isEqualTo(new WorldPositionIntent(new WorldPosition(0, 0)));
    }

    @Test
    void givenFoodIntentNotOnWall_thenIntentUnchanged() {
        var intent = new WorldPosition(1, 1);
        registry.addComponents(
            registry.createEntity(),
            new Food(1),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        registry.addComponents(
            registry.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(2, 2)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodWorldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class))
            .isEqualTo(new WorldPositionIntent(new WorldPosition(1, 1)));
    }
}
