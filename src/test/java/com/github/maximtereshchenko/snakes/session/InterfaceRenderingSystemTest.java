package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.snakes.configuration.Mode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

final class InterfaceRenderingSystemTest {

    private final Camera camera = mock();
    private final Viewport viewport = mock();
    private final SpriteBatch spriteBatch = mock();
    private final BitmapFont bitmapFont = mock();
    private final BitmapFont.BitmapFontData bitmapFontData = mock();
    private final World world = new World();
    private final Mode mode = mock();
    private final InterfaceRenderingSystem interfaceRenderingSystem =
        new InterfaceRenderingSystem(
            world,
            viewport,
            spriteBatch,
            bitmapFont,
            mode
        );

    @BeforeEach
    void setUp() {
        world.addSystems(interfaceRenderingSystem);
    }

    @Test
    void givenInterfaceEntity_thenRendered() {
        when(viewport.getCamera()).thenReturn(camera);
        when(bitmapFont.getData()).thenReturn(bitmapFontData);
        when(mode.palette()).thenReturn(Map.of(Colored.INTERFACE, Color.BLACK));
        world.addComponents(
            world.createEntity(),
            new InterfaceText(2, "score"),
            new InterfacePosition(10, 20),
            Colored.INTERFACE
        );
        world.update(0);
        verify(viewport).apply();
        verify(viewport).getCamera();
        verify(spriteBatch).setProjectionMatrix(camera.combined);
        verify(spriteBatch).begin();
        verify(bitmapFontData).setScale(2);
        verify(bitmapFont).setColor(Color.BLACK);
        verify(bitmapFont).draw(spriteBatch, "score", 10f, 20f);
        verify(bitmapFontData).setScale(anyFloat(), anyFloat());
        verify(spriteBatch).end();
    }
}
