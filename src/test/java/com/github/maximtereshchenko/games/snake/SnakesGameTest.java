package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

final class SnakesGameTest {

    private final Screen loadingScreen = mock();
    private final Screen titleScreen = mock();
    private final Screen modeSelectionScreen = mock();
    private final Screen snakeSessionScreen = mock();
    private final ScreenFactory screenFactory = mock();
    private final Disposable disposable = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final WorldDimensions worldDimensions = new WorldDimensions(0, 0);
    private final SnakeSessionFactory snakeSessionFactory = mock();
    private final UserProfile userProfile = mock();
    private SnakesGame snakesGame;

    private static Stream<ApplicationEvent> modeSelectionScreenEvents() {
        return Stream.of(new TitleScreenFinished(), new SnakeSessionEnded(1));
    }

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
        when(screenFactory.loadingScreen()).thenReturn(loadingScreen);
        snakesGame = new SnakesGame(
            screenFactory,
            worldDimensions,
            Set.of(disposable),
            applicationEvents,
            userProfile
        );
    }

    @Test
    void whenCreated_thenSubscribedAndLoadingScreenShowed() {
        verify(applicationEvents).subscribe(snakesGame);
        verify(loadingScreen).show();
        verify(loadingScreen).resize(anyInt(), anyInt());
    }

    @Test
    void givenAssetsLoaded_thenTitleScreenShowed() {
        when(screenFactory.titleScreen()).thenReturn(titleScreen);
        snakesGame.onEvent(new AssetsLoaded());
        verify(titleScreen).show();
        verify(titleScreen).resize(anyInt(), anyInt());
    }

    @ParameterizedTest
    @MethodSource("modeSelectionScreenEvents")
    void whenApplicationEvent_thenModeSelectionScreenShowed(ApplicationEvent applicationEvent) {
        when(screenFactory.modeSelectionScreen()).thenReturn(modeSelectionScreen);
        snakesGame.onEvent(applicationEvent);
        verify(modeSelectionScreen).show();
        verify(modeSelectionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenModeSelected_thenSnakeSessionScreenShowed() {
        when(screenFactory.snakeSessionScreen(worldDimensions, snakeSessionFactory))
            .thenReturn(snakeSessionScreen);
        snakesGame.onEvent(new ModeSelected(snakeSessionFactory));
        verify(snakeSessionScreen).show();
        verify(snakeSessionScreen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenDisposableDisposed() {
        snakesGame.dispose();
        verify(disposable).dispose();
        verify(userProfile).save();
    }
}