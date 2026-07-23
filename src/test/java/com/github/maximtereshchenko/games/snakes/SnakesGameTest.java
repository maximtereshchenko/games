package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import com.github.maximtereshchenko.games.snakes.event.*;
import com.github.maximtereshchenko.games.snakes.screen.ScreenFactory;
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
    private final UserProfile userProfile = mock();
    private final Mode mode = mock();
    private final SnakesGame snakesGame = new SnakesGame(
        screenFactory,
        Set.of(disposable),
        userProfile
    );

    private static Stream<ApplicationEvent> mainScreenEvents() {
        return Stream.of(
            new TitleScreenFinished(),
            new SnakeSessionEnded(Map.of()),
            new StatisticsScreenFinished(),
            new CreditsScreenFinished(),
            new SettingsScreenFinished()
        );
    }

    @BeforeEach
    void setUp() {
        Gdx.graphics = mock();
    }

    @Test
    void givenAssetsLoaded_thenTitleScreenShowed() {
        when(screenFactory.titleScreen()).thenReturn(screen);
        snakesGame.onEvent(new AssetsLoaded());
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @ParameterizedTest
    @MethodSource("mainScreenEvents")
    void whenApplicationEvent_thenMainScreenShowed(ApplicationEvent applicationEvent) {
        when(screenFactory.mainScreen()).thenReturn(screen);
        snakesGame.onEvent(applicationEvent);
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenModeSelected_thenSnakeSessionScreenShowed() {
        when(screenFactory.snakeSessionScreen(mode)).thenReturn(screen);
        snakesGame.onEvent(new ModeSelected(mode));
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenStatisticsRequested_thenStatisticsScreenShowed() {
        when(screenFactory.statisticsScreen()).thenReturn(screen);
        snakesGame.onEvent(new StatisticsRequested());
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenCreditsRequested_thenCreditsScreenShowed() {
        when(screenFactory.creditsScreen()).thenReturn(screen);
        snakesGame.onEvent(new CreditsRequested());
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenSettingsRequested_thenSettingsScreenShowed() {
        when(screenFactory.settingsScreen()).thenReturn(screen);
        snakesGame.onEvent(new SettingsRequested());
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