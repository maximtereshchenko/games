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

    private final FitViewport fitViewport = spy(new FitViewport(1, 1));
    private final ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);
    private final Dominion dominion = Dominion.create();
    private final AtomicBoolean screenCleared = new AtomicBoolean(false);
    private final StandaloneRenderingSystem standaloneRenderingSystem = new StandaloneRenderingSystem(
        fitViewport,
        shapeRenderer,
        dominion,
        () -> screenCleared.set(true)
    );

    @Test
    void givenEntity_thenEntityRendered() {
        doNothing().when(fitViewport).apply();
        dominion.createEntity(Color.BLACK, new Point(1, 1));
        standaloneRenderingSystem.render();
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