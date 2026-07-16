package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

final class GameRenderingSystemTest {

    private final Camera camera = mock();
    private final Viewport viewport = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final Dominion dominion = Dominion.create();
    private final GameRenderingSystem gameRenderingSystem = new GameRenderingSystem(
        viewport,
        shapeRenderer,
        dominion,
        Map.of(
            Colored.BACKGROUND, Color.WHITE,
            Colored.HEAD, Color.BLACK
        )
    );

    @BeforeEach
    void setUp() {
        Gdx.gl = mock();
    }

    @Test
    void givenEntityWithVisibleAndPosition_thenEntityRendered() {
        when(viewport.getCamera()).thenReturn(camera);
        dominion.createEntity(new Position(1, 1), Colored.HEAD);
        gameRenderingSystem.run(0);
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