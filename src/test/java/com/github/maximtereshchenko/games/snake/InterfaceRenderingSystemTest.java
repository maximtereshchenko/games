package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class InterfaceRenderingSystemTest {

    private final Camera camera = mock();
    private final Viewport viewport = mock();
    private final SpriteBatch spriteBatch = mock();
    private final BitmapFont bitmapFont = mock();
    private final BitmapFont.BitmapFontData bitmapFontData = mock();
    private final Dominion dominion = Dominion.create();
    private final InterfaceRenderingSystem interfaceRenderingSystem = new InterfaceRenderingSystem(
        viewport,
        spriteBatch,
        bitmapFont,
        dominion
    );

    @Test
    void givenFoodEatenCounter_thenFoodEatenRendered() {
        when(viewport.getCamera()).thenReturn(camera);
        when(viewport.getWorldHeight()).thenReturn(20f);
        when(viewport.getWorldWidth()).thenReturn(5f);
        bitmapFontData.scaleX = 1f;
        bitmapFontData.scaleY = 1f;
        when(bitmapFont.getData()).thenReturn(bitmapFontData);
        when(bitmapFont.getCapHeight()).thenReturn(2f);
        dominion.createEntity(new FoodEatenCounter(5));
        try (
            var glyphLayoutMockedConstruction = mockConstruction(
                GlyphLayout.class,
                (glyphLayout, _) -> glyphLayout.width = 1f
            )
        ) {
            interfaceRenderingSystem.run(0);
            verify(viewport).apply();
            verify(viewport).getCamera();
            verify(spriteBatch).setProjectionMatrix(camera.combined);
            verify(spriteBatch).begin();
            verify(bitmapFontData).setScale(1f);

            var constructed = glyphLayoutMockedConstruction.constructed();
            verify(bitmapFont).draw(
                spriteBatch,
                constructed.getFirst(),
                2f,
                20f
            );
            verify(bitmapFontData).setScale(1f, 1f);
            verify(spriteBatch).end();
        }
    }
}
