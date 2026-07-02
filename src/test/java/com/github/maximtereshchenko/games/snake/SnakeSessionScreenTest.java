package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class SnakeSessionScreenTest {

    private final SnakeSessionFactory snakeSessionFactory = mock(SnakeSessionFactory.class);
    private final ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);
    private final FitViewport fitViewport = mock(FitViewport.class);
    private final Dominion dominion = mock(Dominion.class);
    private final Scheduler scheduler = mock(Scheduler.class);
    private final StandaloneRenderingSystem standaloneRenderingSystem = mock(StandaloneRenderingSystem.class);
    private final AtomicBoolean sessionEnded = new AtomicBoolean(false);
    private final WorldDimensions worldDimensions = new WorldDimensions(0, 0);
    @SuppressWarnings("unchecked")
    private final Results<Session> results = mock(Results.class);
    private final SnakeSessionScreen snakeSessionScreen = new SnakeSessionScreen(
        snakeSessionFactory,
        worldDimensions,
        shapeRenderer,
        fitViewport,
        () -> sessionEnded.set(true)
    );

    @BeforeEach
    void setUp() {
        when(snakeSessionFactory.snakeSession(fitViewport, shapeRenderer, worldDimensions))
            .thenReturn(new SnakeSession(dominion, scheduler, standaloneRenderingSystem));
        snakeSessionScreen.show();
    }

    @Test
    void givenSessionRunning_whenRender_thenSchedulerTickedRenderingHappened() {
        when(dominion.findCompositionsWith(Session.class)).thenReturn(results);
        when(results.iterator()).thenReturn(List.of(new Session(Session.Status.RUNNING)).iterator());
        snakeSessionScreen.render(1.0f);
        verify(scheduler).tick(TimeUnit.SECONDS.toNanos(1));
        verify(standaloneRenderingSystem).render();
    }

    @Test
    void givenSessionEnded_whenRender_thenOnSessionEndCalled() {
        when(dominion.findCompositionsWith(Session.class)).thenReturn(results);
        when(results.iterator()).thenReturn(List.of(new Session(Session.Status.ENDED)).iterator());
        snakeSessionScreen.render(1.0f);
        assertThat(sessionEnded).isTrue();
        verifyNoInteractions(scheduler);
        verifyNoInteractions(standaloneRenderingSystem);
    }

    @Test
    void whenResize_thenFitViewportResized() {
        snakeSessionScreen.resize(1, 2);
        verify(fitViewport).update(1, 2, true);
    }

    @Test
    void whenHide_thenSchedulerShutDown() {
        snakeSessionScreen.hide();
        verify(scheduler).shutDown();
    }
}