package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

final class SnakeSessionScreenTest {

    private final SnakeSessionFactory snakeSessionFactory = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final Viewport viewport = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final Dominion dominion = mock();
    private final Scheduler scheduler = mock();
    private final StandaloneRenderingSystem standaloneRenderingSystem = mock();
    private final WorldDimensions worldDimensions = new WorldDimensions(0, 0);
    private final Results<Session> results = mock();
    private final SnakeSessionScreen snakeSessionScreen = new SnakeSessionScreen(
        snakeSessionFactory,
        worldDimensions,
        shapeRenderer,
        viewport,
        applicationEvents
    );

    @Test
    void whenShow_thenSnakeSessionCreated() {
        snakeSessionScreen.show();
        verify(snakeSessionFactory).snakeSession(viewport, shapeRenderer, worldDimensions);
    }

    @Test
    void givenSessionRunning_whenRender_thenSchedulerTickedRenderingHappened() {
        when(snakeSessionFactory.snakeSession(viewport, shapeRenderer, worldDimensions))
            .thenReturn(new SnakeSession(dominion, scheduler, standaloneRenderingSystem));
        when(dominion.findCompositionsWith(Session.class)).thenReturn(results);
        when(results.iterator()).thenReturn(List.of(new Session(Session.Status.RUNNING)).iterator());
        snakeSessionScreen.show();
        snakeSessionScreen.render(1.0f);
        verify(scheduler).tick(TimeUnit.SECONDS.toNanos(1));
        verify(standaloneRenderingSystem).render();
    }

    @Test
    void givenSessionEnded_whenRender_thenOnSessionEndCalled() {
        when(snakeSessionFactory.snakeSession(viewport, shapeRenderer, worldDimensions))
            .thenReturn(new SnakeSession(dominion, scheduler, standaloneRenderingSystem));
        when(dominion.findCompositionsWith(Session.class)).thenReturn(results);
        when(results.iterator()).thenReturn(List.of(new Session(Session.Status.ENDED)).iterator());
        snakeSessionScreen.show();
        snakeSessionScreen.render(1.0f);
        verify(applicationEvents).publish(ApplicationEvent.SNAKE_SESSION_ENDED);
        verifyNoInteractions(scheduler);
        verifyNoInteractions(standaloneRenderingSystem);
    }

    @Test
    void whenResize_thenFitViewportResized() {
        snakeSessionScreen.resize(1, 2);
        verify(viewport).update(1, 2, true);
    }

    @Test
    void whenHide_thenSchedulerShutDown() {
        when(snakeSessionFactory.snakeSession(viewport, shapeRenderer, worldDimensions))
            .thenReturn(new SnakeSession(dominion, scheduler, standaloneRenderingSystem));
        snakeSessionScreen.show();
        snakeSessionScreen.hide();
        verify(scheduler).shutDown();
    }
}