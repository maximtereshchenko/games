package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TurnStartSystemTest {

    private final World world = new World();
    private final Iterable<Entity> turnTimerEntities =
        world.entities(new Query().all(TurnTimer.class));
    private final Iterable<Entity> turnStartedEntities =
        world.entities(new Query().all(TurnStarted.class));
    private final TurnStartSystem turnStartSystem = new TurnStartSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(turnStartSystem);
    }

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        world.addComponents(world.createEntity(), new TurnTimer(1.5f, 0.5f));
        world.update(0.5f);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(1.5f, 1.0f));
        assertThat(turnStartedEntities).isEmpty();
    }

    @Test
    void givenTurnTimerGreaterThatTurnLength_thenTurnStartedEvent() {
        world.addComponents(world.createEntity(), new TurnTimer(0.3f, 0.2f));
        world.update(0.4f);
        assertThat(turnTimerEntities)
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.3f, 0.3f));
        assertThat(turnStartedEntities).hasSize(1);
    }
}
