package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

final class InterfaceRenderingSystemTest {

    private final Camera camera = mock();
    private final Viewport viewport = mock();
    private final SpriteBatch spriteBatch = mock();
    private final BitmapFont bitmapFont = mock();
    private final ScaledFont scaledFont = mock();
    private final Registry registry = new Registry();
    private final Mode mode = mock();
    private final InterfaceRenderingSystem interfaceRenderingSystem =
        new InterfaceRenderingSystem(
            registry,
            viewport,
            spriteBatch,
            scaledFont,
            mode
        );

    @BeforeEach
    void setUp() {
        registry.addSystems(interfaceRenderingSystem);
        doAnswer(
            invocation -> {
                Consumer<BitmapFont> consumer = invocation.getArgument(1);
                consumer.accept(bitmapFont);
                return null;
            }
        )
            .when(scaledFont)
            .use(anyInt(), any());
    }

    @Test
    void givenInterfaceEntity_thenRendered() {
        when(viewport.getCamera()).thenReturn(camera);
        when(mode.palette()).thenReturn(Map.of(PaletteColor.INTERFACE, Color.BLACK));
        registry.addComponents(
            registry.createEntity(),
            new InterfaceText(2, "score"),
            new InterfacePosition(10, 20),
            PaletteColor.INTERFACE
        );
        registry.update(0);
        verify(viewport).apply();
        verify(viewport).getCamera();
        verify(spriteBatch).setProjectionMatrix(camera.combined);
        verify(spriteBatch).begin();
        verify(scaledFont).use(eq(2), any());
        verify(bitmapFont).setColor(Color.BLACK);
        verify(bitmapFont).draw(spriteBatch, "score", 10f, 20f);
        verify(spriteBatch).end();
    }
}
