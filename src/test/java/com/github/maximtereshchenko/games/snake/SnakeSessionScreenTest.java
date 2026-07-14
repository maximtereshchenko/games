package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

final class SnakeSessionScreenTest {

    private final SnakeSessionFactory snakeSessionFactory = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final Viewport viewport = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final Dominion dominion = mock();
    private final System system = mock();
    private final WorldDimensions worldDimensions = new WorldDimensions(0, 0);
    private final SnakeSessionScreen snakeSessionScreen = new SnakeSessionScreen(
        worldDimensions,
        shapeRenderer,
        viewport,
        applicationEvents,
        snakeSessionFactory
    );

    @Test
    void whenShow_thenSnakeSessionCreated() {
        snakeSessionScreen.show();
        verify(snakeSessionFactory).snakeSession(viewport, shapeRenderer, worldDimensions);
    }

    @Test
    void givenSessionRunning_whenRender_thenSchedulerTicked() {
        Results<Session> results = mock();
        when(snakeSessionFactory.snakeSession(viewport, shapeRenderer, worldDimensions))
            .thenReturn(new SnakeSession(dominion, List.of(system)));
        when(dominion.findCompositionsWith(Session.class)).thenReturn(results);
        when(results.iterator()).thenReturn(List.of(new Session(Session.Status.RUNNING)).iterator());
        snakeSessionScreen.show();
        snakeSessionScreen.render(1);
        verify(system).run(1);
    }

    @Test
    void givenSessionEnded_whenRender_thenOnSessionEndCalled() {
        Results<Session> sessionResults = mock();
        Results<LeftTurns> leftTurnsResults = mock();
        when(snakeSessionFactory.snakeSession(viewport, shapeRenderer, worldDimensions))
            .thenReturn(new SnakeSession(dominion, List.of(system)));
        when(dominion.findCompositionsWith(Session.class)).thenReturn(sessionResults);
        when(sessionResults.iterator()).thenReturn(List.of(new Session(Session.Status.ENDED)).iterator());
        when(dominion.findCompositionsWith(LeftTurns.class)).thenReturn(leftTurnsResults);
        when(leftTurnsResults.iterator()).thenReturn(List.of(new LeftTurns(1)).iterator());
        snakeSessionScreen.show();
        snakeSessionScreen.render(1.0f);
        verify(applicationEvents).publish(new SnakeSessionEnded(1));
        verifyNoInteractions(system);
    }

    @Test
    void whenResize_thenFitViewportResized() {
        snakeSessionScreen.resize(1, 2);
        verify(viewport).update(1, 2, true);
    }
}