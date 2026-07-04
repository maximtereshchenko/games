package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

final class SnakesGameTest {

    private final TitleScreen titleScreen = mock();
    private final SnakeSessionScreen snakeSessionScreen = mock();
    private final Disposable disposable = mock();
    private SnakesGame snakesGame;

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
        snakesGame = new SnakesGame(
            titleScreen,
            snakeSessionScreen,
            Set.of(disposable)
        );
    }

    @Test
    void whenCreated_thenTitleScreenShowed() {
        verify(titleScreen).show();
        verify(titleScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenSnakeSessionEnded_thenTitleScreenShowed() {
        reset(titleScreen);
        snakesGame.onEvent(ApplicationEvent.SNAKE_SESSION_ENDED);
        verify(titleScreen).hide();
        verify(titleScreen).show();
        verify(titleScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenContinuedPastTitleScreen_thenSnakeSessionScreenShowed() {
        snakesGame.onEvent(ApplicationEvent.CONTINUED_PAST_TITLE_SCREEN);
        verify(titleScreen).hide();
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenDisposableDisposed() {
        snakesGame.dispose();
        verify(titleScreen).hide();
        verify(disposable).dispose();
    }
}