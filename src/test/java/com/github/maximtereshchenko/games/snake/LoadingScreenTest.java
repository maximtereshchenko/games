package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

final class LoadingScreenTest {

    private final Stage stage = mock();
    private final AssetManager assetManager = mock();
    private final ProgressBar progressBar = mock();
    private final ApplicationEvents applicationEvents = mock();
    private final Viewport viewport = mock();
    private final LoadingScreen loadingScreen = new LoadingScreen(
        stage,
        assetManager,
        progressBar,
        applicationEvents
    );

    @BeforeEach
    void setUp() {
        Gdx.gl = mock();
    }

    @Test
    void whenRender_thenAssetsLoading() {
        loadingScreen.render(1.0f);
        verify(assetManager).update();
        verify(progressBar).setValue(assetManager.getProgress());
        verify(Gdx.gl).glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        verify(Gdx.gl).glClear(anyInt());
        verify(stage).act(1.0f);
        verify(stage).draw();
    }

    @Test
    void givenAssetsLoaded_whenRender_thenAssetsLoadedPublished() {
        when(assetManager.update()).thenReturn(true);
        loadingScreen.render(1.0f);
        verify(progressBar).setValue(assetManager.getProgress());
        verify(applicationEvents).publish(ApplicationEvent.ASSETS_LOADED);
        verify(Gdx.gl).glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        verify(Gdx.gl).glClear(anyInt());
        verify(stage).act(1.0f);
        verify(stage).draw();
    }

    @Test
    void whenResize_thenStageViewportResized() {
        when(stage.getViewport()).thenReturn(viewport);
        loadingScreen.resize(1, 2);
        verify(viewport).update(1, 2, true);
    }

    @Test
    void whenShow_thenInputProcessorSet() {
        Gdx.input = mock();
        loadingScreen.show();
        verify(Gdx.input).setInputProcessor(stage);
    }

    @Test
    void whenHide_thenInputProcessorReset() {
        Gdx.input = mock();
        loadingScreen.hide();
        verify(Gdx.input).setInputProcessor(null);
    }
}