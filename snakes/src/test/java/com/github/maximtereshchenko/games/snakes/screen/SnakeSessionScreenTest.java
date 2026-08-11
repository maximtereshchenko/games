package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.System;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.event.SessionEnded;
import com.github.maximtereshchenko.games.snakes.session.Dead;
import com.github.maximtereshchenko.games.snakes.session.SessionMetric;
import com.github.maximtereshchenko.games.snakes.session.Statistics;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

final class SnakeSessionScreenTest {

    private final Viewport viewport = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final Registry registry = new Registry();
    private final System system = mock();
    private final SessionScreen snakeSessionScreen = new SessionScreen(
        Set.of(viewport),
        applicationEvents,
        registry
    );

    @Test
    void givenNoDead_whenRender_thenSystemUpdated() {
        registry.addSystems(system);
        snakeSessionScreen.render(1);
        verify(system).update(any(), eq(1.0f));
        verifyNoInteractions(applicationEvents);
    }

    @Test
    void givenDead_whenRender_thenOnSessionEndCalled() {
        registry.addComponents(registry.createEntity(), Dead.INSTANCE);
        registry.addComponents(registry.createEntity(), new Statistics(Map.of()));
        registry.addSystems(system);
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
        registry.addSystems(system);
        snakeSessionScreen.show();
        verify(system).update(any(), eq(0.0f));
    }
}
