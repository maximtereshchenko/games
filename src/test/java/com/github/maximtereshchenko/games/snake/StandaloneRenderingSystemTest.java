package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class StandaloneRenderingSystemTest {

    private final Camera camera = mock();
    private final Viewport viewport = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final Dominion dominion = Dominion.create();
    private final StandaloneRenderingSystem standaloneRenderingSystem = new StandaloneRenderingSystem(
        viewport,
        shapeRenderer,
        dominion
    );

    @BeforeEach
    void setUp() {
        Gdx.gl = mock();
    }

    @Test
    void givenEntity_thenEntityRendered() {
        doNothing().when(viewport).apply();
        when(viewport.getCamera()).thenReturn(camera);
        dominion.createEntity(new Position(1, 1), new Visible(Color.BLACK));
        standaloneRenderingSystem.render();
        verify(Gdx.gl).glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        verify(Gdx.gl).glClear(anyInt());
        verify(viewport).apply();
        verify(viewport).getCamera();
        verify(shapeRenderer).setProjectionMatrix(camera.combined);
        verify(shapeRenderer).begin(ShapeRenderer.ShapeType.Filled);
        verify(shapeRenderer).setColor(Color.WHITE);
        verify(shapeRenderer)
            .rect(
                0,
                0,
                viewport.getScreenWidth(),
                viewport.getScreenHeight()
            );
        verify(shapeRenderer).setColor(Color.BLACK);
        verify(shapeRenderer).rect(1, 1, 1, 1);
        verify(shapeRenderer).end();
    }
}