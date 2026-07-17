package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.github.maximtereshchenko.games.snakes.event.*;
import com.github.maximtereshchenko.games.snakes.screen.ScreenFactory;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

final class SnakesGameTest {

    private final Screen screen = mock();
    private final ScreenFactory screenFactory = mock();
    private final Disposable disposable = mock();
    private final WorldDimensions worldDimensions = new WorldDimensions(0, 0);
    private final UserProfile userProfile = mock();
    private final Mode mode = mock();
    private SnakesGame snakesGame;

    private static Stream<ApplicationEvent> modeSelectionScreenEvents() {
        return Stream.of(
            new TitleScreenFinished(),
            new SnakeSessionEnded(Map.of()),
            new StatisticsScreenFinished()
        );
    }

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
        when(screenFactory.loadingScreen()).thenReturn(screen);
        snakesGame = new SnakesGame(
            screenFactory,
            worldDimensions,
            Set.of(disposable),
            userProfile
        );
    }

    @Test
    void givenAssetsLoaded_thenTitleScreenShowed() {
        when(screenFactory.titleScreen()).thenReturn(screen);
        snakesGame.onEvent(new AssetsLoaded());
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @ParameterizedTest
    @MethodSource("modeSelectionScreenEvents")
    void whenApplicationEvent_thenModeSelectionScreenShowed(ApplicationEvent applicationEvent) {
        when(screenFactory.modeSelectionScreen()).thenReturn(screen);
        snakesGame.onEvent(applicationEvent);
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenModeSelected_thenSnakeSessionScreenShowed() {
        when(screenFactory.snakeSessionScreen(worldDimensions, mode))
            .thenReturn(screen);
        snakesGame.onEvent(new ModeSelected(mode));
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenStatisticsScreenFinished_thenStatisticsShowed() {
        when(screenFactory.statisticsScreen()).thenReturn(screen);
        snakesGame.onEvent(new StatisticsRequested());
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenDisposableDisposed() {
        snakesGame.dispose();
        verify(disposable).dispose();
        verify(userProfile).save();
    }
}