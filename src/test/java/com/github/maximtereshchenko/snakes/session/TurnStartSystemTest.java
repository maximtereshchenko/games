package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class TurnStartSystemTest {

    private final World world = new World();
    private final EntityFactory entityFactory = mock();
    private final TurnStartSystem turnStartSystem =
        new TurnStartSystem(world, entityFactory);

    @BeforeEach
    void setUp() {
        world.addSystems(turnStartSystem);
    }

    @Test
    void givenDeltaLessThanTurnLength_thenStopwatchIncremented() {
        world.addComponents(world.createEntity(), new TurnTimer(1.5f, 0.5f));
        world.update(0.5f);
        assertThat(world.entities(new Query().all(TurnTimer.class)))
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(1.5f, 1.0f));
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnTimerGreaterThatTurnLength_thenTurnStartedEvent() {
        world.addComponents(world.createEntity(), new TurnTimer(0.3f, 0.2f));
        world.update(0.4f);
        assertThat(world.entities(new Query().all(TurnTimer.class)))
            .singleElement()
            .extracting(entity -> entity.component(TurnTimer.class))
            .usingRecursiveComparison()
            .isEqualTo(new TurnTimer(0.3f, 0.3f));
        verify(entityFactory).createTurnStartedEvent(any(WorldEdit.class));
    }
}