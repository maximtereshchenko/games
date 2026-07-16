package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.maximtereshchenko.games.snakes.Assets;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.event.AssetsLoaded;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

final class LoadingScreenTest {

    private final StageScreen stageScreen = mock();
    private final AssetManager assetManager = mock();
    private final ProgressBar progressBar = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final Assets assets = mock();
    private final AssetDescriptor<?> assetDescriptor = mock();
    private final LoadingScreen loadingScreen = new LoadingScreen(
        stageScreen,
        assetManager,
        progressBar,
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
    void givenStageScreen_thenMethodDelegated(Consumer<Screen> method) {
        method.accept(loadingScreen);
        method.accept(verify(stageScreen));
    }

    @Test
    void whenRender_thenAssetsLoading() {
        loadingScreen.render(1.0f);
        verify(assetManager).update();
        verify(progressBar).setValue(assetManager.getProgress());
        verify(stageScreen).render(1.0f);
    }

    @Test
    void givenAssetsLoaded_whenRender_thenAssetsLoadedPublished() {
        when(assetManager.update()).thenReturn(true);
        loadingScreen.render(1.0f);
        verify(progressBar).setValue(assetManager.getProgress());
        verify(stageScreen).render(1.0f);
        verify(applicationEvents).publish(any(AssetsLoaded.class));

    }

    @Test
    void whenShow_thenInputProcessorSet() {
        when(assets.gameAssets()).thenReturn(Set.of(assetDescriptor));
        loadingScreen.show();
        verify(assetManager).load(assetDescriptor);
        verify(stageScreen).show();
    }
}