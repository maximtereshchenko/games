package com.github.maximtereshchenko.snakes.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.System;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.snakes.event.SessionEnded;
import com.github.maximtereshchenko.snakes.session.Dead;
import com.github.maximtereshchenko.snakes.session.SessionMetric;
import com.github.maximtereshchenko.snakes.session.Statistics;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

final class SnakeSessionScreenTest {

    private final Viewport viewport = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final World world = new World();
    private final System system = mock();
    private final SessionScreen snakeSessionScreen = new SessionScreen(
        Set.of(viewport),
        applicationEvents,
        world
    );

    @Test
    void givenNoDead_whenRender_thenSystemUpdated() {
        world.addSystems(system);
        snakeSessionScreen.render(1);
        verify(system).update(any(), eq(1.0f));
        verifyNoInteractions(applicationEvents);
    }

    @Test
    void givenDead_whenRender_thenOnSessionEndCalled() {
        world.addComponents(world.createEntity(), Dead.INSTANCE);
        world.addComponents(world.createEntity(), new Statistics(Map.of()));
        world.addSystems(system);
        snakeSessionScreen.render(1.0f);
        verify(system).update(any(), eq(1.0f));
        verify(applicationEvents)
            .publish(
                new SessionEnded(
                    Map.of(
                        SessionMetric.LEFT_TURNS, 0,
                        SessionMetric.FOOD_CONSUMED, 0,
                        SessionMetric.WARPS, 0
                    )
                )
            );
    }

    @Test
    void whenResize_thenFitViewportResized() {
        snakeSessionScreen.resize(1, 2);
        verify(viewport).update(1, 2, true);
    }

    @Test
    void whenShow_thenSystemUpdated() {
        world.addSystems(system);
        snakeSessionScreen.show();
        verify(system).update(any(), eq(0.0f));
    }
}
