package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TurnStartSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> turnTimerEntities =
        registry.entities(new Query().all(TurnTimer.class));
    private final Iterable<Entity> turnStartedEntities =
        registry.entities(new Query().all(TurnStarted.class));
    private final TurnStartSystem turnStartSystem = new TurnStartSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(turnStartSystem);
    }

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        registry.addComponents(registry.createEntity(), new TurnTimer(1.5f, 0.5f));
        registry.update(0.5f);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(1.5f, 1.0f));
        assertThat(turnStartedEntities).isEmpty();
    }

    @Test
    void givenTurnTimerGreaterThatTurnLength_thenTurnStartedEvent() {
        registry.addComponents(registry.createEntity(), new TurnTimer(0.3f, 0.2f));
        registry.update(0.4f);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.3f, 0.3f));
        assertThat(turnStartedEntities).hasSize(1);
    }
}
