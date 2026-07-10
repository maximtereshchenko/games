package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

final class SnakesGameTest {

    private final Screen loadingScreen = mock();
    private final Screen titleScreen = mock();
    private final Screen modeSelectionScreen = mock();
    private final Screen snakeSessionScreen = mock();
    private final Disposables disposables = mock();
    private SnakesGame snakesGame;

    private static Stream<ApplicationEvent> modeSelectionScreenEvents() {
        return Stream.of(new TitleScreenFinished(), new SnakeSessionEnded(1));
    }

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
        snakesGame = new SnakesGame(
            loadingScreen,
            titleScreen,
            modeSelectionScreen,
            snakeSessionScreen,
            disposables
        );
    }

    @Test
    void whenCreated_thenLoadingScreenShowed() {
        verify(loadingScreen).show();
        verify(loadingScreen).resize(anyInt(), anyInt());
    }

    @Test
    void givenAssetsLoaded_thenTitleScreenShowed() {
        snakesGame.onEvent(new AssetsLoaded());
        verify(titleScreen).show();
        verify(titleScreen).resize(anyInt(), anyInt());
    }

    @ParameterizedTest
    @MethodSource("modeSelectionScreenEvents")
    void whenApplicationEvent_thenModeSelectionScreenShowed(ApplicationEvent applicationEvent) {
        snakesGame.onEvent(applicationEvent);
        verify(modeSelectionScreen).show();
        verify(modeSelectionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenModeSelected_thenSnakeSessionScreenShowed() {
        snakesGame.onEvent(new ModeSelected(null));
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenDisposableDisposed() {
        snakesGame.dispose();
        verify(disposables).dispose();
    }
}