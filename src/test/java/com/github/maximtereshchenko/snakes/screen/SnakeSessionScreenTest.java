package com.github.maximtereshchenko.snakes.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.System;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.snakes.session.Session;
import com.github.maximtereshchenko.snakes.session.SessionStatistics;
import com.github.maximtereshchenko.snakes.session.SessionStatisticsAccumulator;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

final class SnakeSessionScreenTest {

    private final Viewport viewport = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final World world = new World();
    private final System system = mock();
    private final SnakeSessionScreen snakeSessionScreen = new SnakeSessionScreen(
        Set.of(viewport),
        applicationEvents,
        world
    );

    @Test
    void givenSessionRunning_whenRender_thenSystemUpdated() {
        world.addComponents(world.createEntity(), new Session(Session.Status.RUNNING));
        world.addSystems(system);
        snakeSessionScreen.render(1);
        verify(system).update(any(), eq(1.0f));
    }

    @Test
    void givenSessionEnded_whenRender_thenOnSessionEndCalled() {
        world.addComponents(world.createEntity(), new Session(Session.Status.ENDED));
        world.addComponents(world.createEntity(), new SessionStatisticsAccumulator());
        world.addSystems(system);
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