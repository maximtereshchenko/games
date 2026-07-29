package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class TurnLengthScalingSystemTest {

    private final World world = new World();
    private final Iterable<Entity> turnTimerEntities = world.entities(
        new Query().all(TurnTimer.class)
    );
    private final TurnLengthScalingSystem turnLengthScalingSystem =
        new TurnLengthScalingSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(turnLengthScalingSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new TurnTimer(1, 2),
            new TurnLengthScaling(3, 4, 5, 6)
        );
        world.addComponents(
            world.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 7))
        );
        world.update(0);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(1, 2));
    }

    @Test
    void givenFoodConsumed_thenNewTurnLengthCalculated() {
        world.addComponents(
            world.createEntity(),
            new TurnTimer(1, 0),
            new TurnLengthScaling(1, 2, 0.1f, 0.2f)
        );
        world.addComponents(
            world.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 5))
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.update(0);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.8f, 0));
    }

    @Test
    void givenManyFoodConsumed_thenMinimalTurnLength() {
        world.addComponents(
            world.createEntity(),
            new TurnTimer(1, 0),
            new TurnLengthScaling(1, 2, 0.1f, 0.2f)
        );
        world.addComponents(
            world.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 20))
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.update(0);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.2f, 0));
    }
}
