package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class SnakesGameTest {

    private final ShapeRenderer shapeRenderer = mock();
    private final SnakeSessionScreen snakeSessionScreen = mock();
    private SnakesGame snakesGame;

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
        snakesGame = new SnakesGame(
            shapeRenderer,
            snakeSessionScreen
        );
    }

    @Test
    void whenCreated_thenSnakeSessionScreenShowed() {
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenSnakeSessionEnded_thenSnakeSessionScreenShowed() {
        reset(snakeSessionScreen);
        snakesGame.onEvent(ApplicationEvent.SNAKE_SESSION_ENDED);
        verify(snakeSessionScreen).hide();
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenShapeRendererDisposed() {
        snakesGame.dispose();
        verify(snakeSessionScreen).hide();
        verify(shapeRenderer).dispose();
    }
}