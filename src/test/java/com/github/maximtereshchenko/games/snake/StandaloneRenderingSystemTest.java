package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class StandaloneRenderingSystemTest {

    @Test
    void givenEntity_thenEntityRendered() {
        var fitViewport = spy(new FitViewport(1, 1));
        doNothing().when(fitViewport).apply();
        var shapeRenderer = mock(ShapeRenderer.class);
        var dominion = Dominion.create();
        dominion.createEntity(Color.BLACK, new Point(1, 1));
        var screenCleared = new AtomicBoolean(false);
        new StandaloneRenderingSystem(
            fitViewport,
            shapeRenderer,
            dominion,
            () -> screenCleared.set(true)
        )
            .render();
        assertThat(screenCleared).isTrue();
        verify(fitViewport).apply();
        verify(fitViewport).getCamera();
        verify(shapeRenderer).setProjectionMatrix(fitViewport.getCamera().combined);
        verify(shapeRenderer).begin(ShapeRenderer.ShapeType.Filled);
        verify(shapeRenderer).setColor(Color.WHITE);
        verify(shapeRenderer)
            .rect(
                0,
                0,
                fitViewport.getScreenWidth(),
                fitViewport.getScreenHeight()
            );
        verify(shapeRenderer).setColor(Color.BLACK);
        verify(shapeRenderer).rect(1, 1, 1, 1);
        verify(shapeRenderer).end();
    }
}