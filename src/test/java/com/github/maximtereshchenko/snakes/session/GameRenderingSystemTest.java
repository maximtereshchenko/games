package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.snakes.configuration.Mode;
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
    private final Mode mode = mock();
    private final GameRenderingSystem gameRenderingSystem = new GameRenderingSystem(
        viewport,
        shapeRenderer,
        dominion,
        mode
    );

    @BeforeEach
    void setUp() {
        Gdx.gl = mock();
    }

    @Test
    void givenEntities_thenBackgroundEntitiesRenderedFirst() {
        when(viewport.getCamera()).thenReturn(camera);
        when(mode.palette())
            .thenReturn(
                Map.of(
                    Colored.BACKGROUND, Color.WHITE,
                    Colored.HEAD, Color.BLACK
                )
            );
        dominion.createEntity(new Position(0, 0), Colored.HEAD);
        dominion.createEntity(new Position(1, 1), Colored.BACKGROUND, Background.INSTANCE);
        gameRenderingSystem.run(0);
        verify(Gdx.gl).glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        verify(Gdx.gl).glClear(anyInt());
        verify(viewport).apply();
        verify(viewport).getCamera();
        var order = inOrder(shapeRenderer);
        order.verify(shapeRenderer).setProjectionMatrix(camera.combined);
        order.verify(shapeRenderer).begin(ShapeRenderer.ShapeType.Filled);
        order.verify(shapeRenderer).setColor(Color.WHITE);
        order.verify(shapeRenderer).rect(1, 1, 1, 1);
        order.verify(shapeRenderer).setColor(Color.BLACK);
        order.verify(shapeRenderer).rect(0, 0, 1, 1);
        order.verify(shapeRenderer).end();
    }
}