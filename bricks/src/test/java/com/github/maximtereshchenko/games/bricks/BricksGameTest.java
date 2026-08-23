package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.github.maximtereshchenko.games.bricks.event.*;
import com.github.maximtereshchenko.games.bricks.screen.ScreenFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

final class BricksGameTest {

    private final Screen screen = mock();
    private final ScreenFactory screenFactory = mock();
    private final Disposable disposable = mock();
    private final UserProfile userProfile = mock();
    private final Graphics graphics = mock();
    private final BricksGame bricksGame = new BricksGame(
        screenFactory,
        userProfile,
        Set.of(disposable)
    );

    private static Stream<Event> mainScreenEvents() {
        return Stream.of(
            new AssetsLoaded(),
            new LevelCompleted("easy", 1, 2),
            new LevelFailed(),
            new SettingsScreenFinished()
        );
    }

    @BeforeEach
    void setUp() {
        Gdx.graphics = graphics;
    }

    @ParameterizedTest
    @MethodSource("mainScreenEvents")
    void whenEvent_thenMainScreenShowed(Event event) {
        when(screenFactory.mainScreen()).thenReturn(screen);
        bricksGame.onEvent(event);
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDifficultySelected_thenLevelSelectionScreenShowed() {
        when(screenFactory.levelSelectionScreen("easy")).thenReturn(screen);
        bricksGame.onEvent(new DifficultySelected("easy"));
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenLevelSelected_thenSessionScreenShowed() {
        when(screenFactory.sessionScreen("easy", 2)).thenReturn(screen);
        bricksGame.onEvent(new LevelSelected("easy", 2));
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDifficultySelectionRequested_thenDifficultySelectionScreenShowed() {
        when(screenFactory.difficultySelectionScreen()).thenReturn(screen);
        bricksGame.onEvent(new DifficultySelectionRequested());
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenSettingsRequested_thenSettingsScreenShowed() {
        when(screenFactory.settingsScreen()).thenReturn(screen);
        bricksGame.onEvent(new SettingsRequested());
        verify(screen).show();
        verify(screen).resize(anyInt(), anyInt());
    }

    @Test
    void whenDispose_thenScreenHiddenDisposableDisposed() {
        bricksGame.dispose();
        verify(disposable).dispose();
        verify(userProfile).save();
    }
}
