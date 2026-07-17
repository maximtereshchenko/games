package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.snakes.Assets;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.event.AssetsLoaded;
import com.github.maximtereshchenko.games.snakes.screen.view.LoadingView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

final class LoadingScreenTest {

    private final Screen screen = mock();
    private final LoadingView loadingView = mock();
    private final AssetManager assetManager = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final Assets assets = mock();
    private final AssetDescriptor<?> assetDescriptor = mock();
    private final LoadingScreen loadingScreen = new LoadingScreen(
        screen,
        loadingView,
        assetManager,
        applicationEvents,
        assets
    );

    private static Stream<Consumer<Screen>> delegatingMethods() {
        return Stream.of(
            screen -> screen.resize(1, 2),
            Screen::pause,
            Screen::resume,
            Screen::hide,
            Screen::dispose
        );
    }

    @ParameterizedTest
    @MethodSource("delegatingMethods")
    void givenScreen_thenMethodDelegated(Consumer<Screen> method) {
        method.accept(loadingScreen);
        method.accept(verify(screen));
    }

    @Test
    void whenRender_thenAssetsLoading() {
        when(assetManager.getProgress()).thenReturn(0.5f);
        loadingScreen.render(1.0f);
        verify(assetManager).update();
        verify(loadingView).updateProgress(0.5f);
        verify(screen).render(1.0f);
    }

    @Test
    void givenAssetsLoaded_whenRender_thenAssetsLoadedPublished() {
        when(assetManager.update()).thenReturn(true);
        when(assetManager.getProgress()).thenReturn(0.5f);
        loadingScreen.render(1.0f);
        verify(loadingView).updateProgress(0.5f);
        verify(screen).render(1.0f);
        verify(applicationEvents).publish(any(AssetsLoaded.class));

    }

    @Test
    void whenShow_thenInputProcessorSet() {
        when(assets.gameAssets()).thenReturn(Set.of(assetDescriptor));
        loadingScreen.show();
        verify(assetManager).load(assetDescriptor);
        verify(screen).show();
    }
}