package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class WorldRenderingSystemTest {

    private final Camera camera = mock();
    private final Viewport viewport = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final Registry registry = new Registry();
    private final Mode mode = mock();
    private final Color backgroundColor = new Color();
    private final Color headColor = new Color();
    private final WorldRenderingSystem worldRenderingSystem = new WorldRenderingSystem(
        registry,
        viewport,
        shapeRenderer,
        mode
    );

    @BeforeEach
    void setUp() {
        registry.addSystems(worldRenderingSystem);
        Gdx.gl = mock();
    }

    @Test
    void givenEntities_thenBackgroundEntitiesRenderedFirst() {
        when(viewport.getCamera()).thenReturn(camera);
        when(mode.palette())
            .thenReturn(
                Map.of(
                    PaletteColor.BACKGROUND, Color.WHITE,
                    PaletteColor.HEAD, Color.BLACK
                )
            );
        when(shapeRenderer.getColor()).thenReturn(backgroundColor, headColor);
        registry.addComponents(
            registry.createEntity(),
            new WorldPosition(0, 0),
            PaletteColor.HEAD,
            new Opacity(1)
        );
        registry.addComponents(
            registry.createEntity(),
            new WorldPosition(1, 1),
            PaletteColor.BACKGROUND,
            Background.INSTANCE,
            new Opacity(0.5f)
        );
        registry.update(0);
        verify(Gdx.gl).glClearColor(
            Color.BLACK.r,
            Color.BLACK.g,
            Color.BLACK.b,
            Color.BLACK.a
        );
        verify(Gdx.gl).glClear(anyInt());
        verify(viewport).apply();
        verify(viewport).getCamera();
        var order = inOrder(shapeRenderer);
        order.verify(shapeRenderer).setProjectionMatrix(camera.combined);
        order.verify(shapeRenderer).begin(ShapeRenderer.ShapeType.Filled);
        order.verify(shapeRenderer).rect(1, 1, 1, 1);
        order.verify(shapeRenderer).rect(0, 0, 1, 1);
        order.verify(shapeRenderer).end();
        assertThat(backgroundColor).isEqualTo(new Color(1, 1, 1, 0.5f));
        assertThat(headColor).isEqualTo(Color.BLACK);
    }
}
