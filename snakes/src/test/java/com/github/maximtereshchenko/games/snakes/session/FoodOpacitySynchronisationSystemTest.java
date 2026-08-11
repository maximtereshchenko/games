package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodOpacitySynchronisationSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> foodEntities =
        registry.entities(new Query().all(Food.class, Opacity.class));
    private final FoodOpacitySynchronisationSystem foodOpacitySynchronisationSystem =
        new FoodOpacitySynchronisationSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(foodOpacitySynchronisationSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            new Food(0.5f),
            new Opacity(1.0f)
        );
        registry.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Opacity.class).value)
            .isEqualTo(1.0f);
    }

    @Test
    void givenTurnStartedEvent_thenOpacitySynchronisedWithGrowth() {
        registry.addComponents(
            registry.createEntity(),
            new Food(0.5f),
            new Opacity(1.0f)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities)
            .singleElement()
            .extracting(entity -> entity.component(Opacity.class).value)
            .isEqualTo(0.5f);
    }

    @Test
    void givenManyFood_thenEachOpacitySynchronisedWithGrowth() {
        registry.addComponents(
            registry.createEntity(),
            new Food(0.3f),
            new Opacity(1.0f)
        );
        registry.addComponents(
            registry.createEntity(),
            new Food(0.7f),
            new Opacity(1.0f)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(foodEntities)
            .extracting(entity -> entity.component(Opacity.class).value)
            .containsExactlyInAnyOrder(0.3f, 0.7f);
    }
}
