package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

final class StageScreenTest {

    private final Stage stage = mock();
    private final Viewport viewport = mock();
    private final StageScreen stageScreen = new StageScreen(stage);

    @Test
    void whenRender_thenStageDrawn() {
        Gdx.gl = mock();
        stageScreen.render(1.0f);
        verify(Gdx.gl).glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        verify(Gdx.gl).glClear(anyInt());
        verify(stage).act(1.0f);
        verify(stage).draw();
    }

    @Test
    void whenResize_thenStageViewportResized() {
        when(stage.getViewport()).thenReturn(viewport);
        stageScreen.resize(1, 2);
        verify(viewport).update(1, 2, true);
    }

    @Test
    void whenShow_thenInputProcessorSet() {
        Gdx.input = mock();
        stageScreen.show();
        verify(Gdx.input).setInputProcessor(stage);
    }

    @Test
    void whenHide_thenInputProcessorReset() {
        Gdx.input = mock();
        stageScreen.hide();
        verify(Gdx.input).setInputProcessor(null);
    }
}