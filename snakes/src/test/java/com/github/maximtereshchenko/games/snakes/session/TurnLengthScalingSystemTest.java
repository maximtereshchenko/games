package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class TurnLengthScalingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> turnTimerEntities = registry.view(
        new Query().all(TurnTimer.class)
    );
    private final TurnLengthScalingSystem turnLengthScalingSystem =
        new TurnLengthScalingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(turnLengthScalingSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            new TurnTimer(1, 2),
            new TurnLengthScaling(3, 4, 5, 6)
        );
        registry.addComponents(
            registry.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 7))
        );
        registry.update(0);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(1, 2));
    }

    @Test
    void givenFoodConsumed_thenNewTurnLengthCalculated() {
        registry.addComponents(
            registry.createEntity(),
            new TurnTimer(1, 0),
            new TurnLengthScaling(1, 2, 0.1f, 0.2f)
        );
        registry.addComponents(
            registry.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 5))
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.addComponents(registry.createEntity(), new FoodConsumed(1));
        registry.update(0);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.8f, 0));
    }

    @Test
    void givenManyFoodConsumed_thenMinimalTurnLength() {
        registry.addComponents(
            registry.createEntity(),
            new TurnTimer(1, 0),
            new TurnLengthScaling(1, 2, 0.1f, 0.2f)
        );
        registry.addComponents(
            registry.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 20))
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.addComponents(registry.createEntity(), new FoodConsumed(1));
        registry.update(0);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.2f, 0));
    }
}
