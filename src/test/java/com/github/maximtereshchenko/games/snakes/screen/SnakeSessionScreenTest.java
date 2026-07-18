package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.games.snakes.session.Session;
import com.github.maximtereshchenko.games.snakes.session.SessionStatistics;
import com.github.maximtereshchenko.games.snakes.session.SessionStatisticsAccumulator;
import com.github.maximtereshchenko.games.snakes.session.System;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

final class SnakeSessionScreenTest {

    private final Viewport viewport = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final Dominion dominion = Dominion.create();
    private final System system = mock();
    private final SnakeSessionScreen snakeSessionScreen = new SnakeSessionScreen(
        Set.of(viewport),
        applicationEvents,
        dominion,
        List.of(system)
    );

    @Test
    void givenSessionRunning_whenRender_thenSchedulerTicked() {
        dominion.createEntity(new Session(Session.Status.RUNNING));
        snakeSessionScreen.render(1);
        verify(system).run(1);
    }

    @Test
    void givenSessionEnded_whenRender_thenOnSessionEndCalled() {
        dominion.createEntity(new Session(Session.Status.ENDED));
        dominion.createEntity(new SessionStatisticsAccumulator());
        snakeSessionScreen.render(1.0f);
        verify(applicationEvents)
            .publish(new SnakeSessionEnded(Map.of(SessionStatistics.LEFT_TURNS, 0)));
        verifyNoInteractions(system);
    }

    @Test
    void whenResize_thenFitViewportResized() {
        snakeSessionScreen.resize(1, 2);
        verify(viewport).update(1, 2, true);
    }
}