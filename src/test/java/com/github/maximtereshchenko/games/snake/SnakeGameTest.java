package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class SnakeGameTest {

    private final ScreenFactory screenFactory = mock(ScreenFactory.class);
    private final ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);
    private final SnakeSessionScreen snakeSessionScreen = mock(SnakeSessionScreen.class);
    private final SnakeGame snakeGame = new SnakeGame(screenFactory, () -> shapeRenderer);

    @Test
    void whenDispose_thenScreenHiddenShapeRendererDisposed() {
        Gdx.graphics = mock(Graphics.class);
        when(screenFactory.snakeSessionScreen(eq(shapeRenderer), any()))
            .thenReturn(snakeSessionScreen);
        snakeGame.create();
        verify(snakeSessionScreen).show();
        snakeGame.dispose();
        verify(snakeSessionScreen).hide();
        verify(shapeRenderer).dispose();
    }
}