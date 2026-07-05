package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.Mockito.*;

final class SnakesGameTest {

    private final Screen loadingScreen = mock();
    private final Screen titleScreen = mock();
    private final Screen snakeSessionScreen = mock();
    private final Disposables disposables = mock();
    private SnakesGame snakesGame;

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
        snakesGame = new SnakesGame(
            loadingScreen,
            titleScreen,
            snakeSessionScreen,
            disposables
        );
    }

    @Test
    void whenCreated_thenLoadingScreenShowed() {
        verify(loadingScreen).show();
        verify(loadingScreen).resize(anyInt(), anyInt());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ASSETS_LOADED", "SNAKE_SESSION_ENDED"})
    void givenApplicationEvent_thenTitleScreenShowed(ApplicationEvent applicationEvent) {
        snakesGame.onEvent(applicationEvent);
        verify(titleScreen).show();
        verify(titleScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenContinuedPastTitleScreen_thenSnakeSessionScreenShowed() {
        snakesGame.onEvent(ApplicationEvent.CONTINUED_PAST_TITLE_SCREEN);
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenDisposableDisposed() {
        snakesGame.dispose();
        verify(loadingScreen).hide();
        verify(disposables).dispose();
    }
}