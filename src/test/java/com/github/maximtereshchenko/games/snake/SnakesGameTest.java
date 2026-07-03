package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class SnakesGameTest {

    private final ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);
    private final SnakeSessionScreen snakeSessionScreen = mock(SnakeSessionScreen.class);
    private Runnable onSessionEnd;
    private final SnakesGame snakesGame = new SnakesGame(
        () -> shapeRenderer,
        (_, runnable) -> snakeSessionScreen(runnable)
    );

    @Test
    void whenSnakeSessionEnds_thenAnotherSnakeSessionStarted() {
        Gdx.graphics = mock(Graphics.class);
        snakesGame.create();
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
        reset(snakeSessionScreen);
        onSessionEnd.run();
        verify(snakeSessionScreen).hide();
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenShapeRendererDisposed() {
        Gdx.graphics = mock(Graphics.class);
        snakesGame.create();
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
        snakesGame.dispose();
        verify(snakeSessionScreen).hide();
        verify(shapeRenderer).dispose();
    }

    private SnakeSessionScreen snakeSessionScreen(Runnable runnable) {
        onSessionEnd = runnable;
        return snakeSessionScreen;
    }
}