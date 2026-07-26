package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TimerSystemTest {

    private final World world = new World();
    private final TimerSystem timerDecrementSystem =
        new TimerSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(timerDecrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new Timer(1, 1));
        world.update(0);
        assertThat(world.entities(new Query().all(Timer.class)))
            .singleElement()
            .extracting(entity -> entity.component(Timer.class).turnsRemaining)
            .isEqualTo(1);
    }

    @Test
    void givenTurnStartedEvent_thenTimerDecremented() {
        world.addComponents(world.createEntity(), new Timer(1, 1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Timer.class)))
            .singleElement()
            .extracting(entity -> entity.component(Timer.class).turnsRemaining)
            .isEqualTo(0);
    }
}