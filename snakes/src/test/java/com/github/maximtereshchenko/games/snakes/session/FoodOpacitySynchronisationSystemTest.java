package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodOpacitySynchronisationSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodEntities =
        world.entities(new Query().all(Food.class, Opacity.class));
    private final FoodOpacitySynchronisationSystem foodOpacitySynchronisationSystem =
        new FoodOpacitySynchronisationSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(foodOpacitySynchronisationSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new Food(0.5f),
            new Opacity(1.0f)
        );
        world.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Opacity.class).value)
            .isEqualTo(1.0f);
    }

    @Test
    void givenTurnStartedEvent_thenOpacitySynchronisedWithGrowth() {
        world.addComponents(
            world.createEntity(),
            new Food(0.5f),
            new Opacity(1.0f)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Opacity.class).value)
            .isEqualTo(0.5f);
    }

    @Test
    void givenManyFood_thenEachOpacitySynchronisedWithGrowth() {
        world.addComponents(
            world.createEntity(),
            new Food(0.3f),
            new Opacity(1.0f)
        );
        world.addComponents(
            world.createEntity(),
            new Food(0.7f),
            new Opacity(1.0f)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(foodEntities)
            .extracting(entity -> entity.component(Opacity.class).value)
            .containsExactlyInAnyOrder(0.3f, 0.7f);
    }
}
