package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class InterfaceRenderingSystemTest {

    private final Camera camera = mock();
    private final Viewport viewport = mock();
    private final SpriteBatch spriteBatch = mock();
    private final BitmapFont bitmapFont = mock();
    private final BitmapFont.BitmapFontData bitmapFontData = mock();
    private final Dominion dominion = Dominion.create();
    private final Mode mode = mock();
    private final InterfaceRenderingSystem interfaceRenderingSystem = new InterfaceRenderingSystem(
        viewport,
        spriteBatch,
        bitmapFont,
        dominion,
        mode
    );

    private static Stream<Object> components() {
        return Stream.of(new FoodEatenCounter(1), new AirCounter(1, 1));
    }

    @ParameterizedTest
    @MethodSource("components")
    void givenComponent_thenRendered(Object component) {
        when(viewport.getCamera()).thenReturn(camera);
        when(bitmapFont.getData()).thenReturn(bitmapFontData);
        when(mode.palette()).thenReturn(Map.of(Colored.INTERFACE, Color.BLACK));
        dominion.createEntity(component, Colored.INTERFACE);
        try (
            var _ = mockConstruction(
                GlyphLayout.class,
                (_, context) -> assertThat(context.arguments())
                    .anySatisfy(argument -> assertThat(argument).isEqualTo(Color.BLACK))
            )
        ) {
            interfaceRenderingSystem.run(0);
            verify(viewport).apply();
            verify(viewport).getCamera();
            verify(spriteBatch).setProjectionMatrix(camera.combined);
            verify(spriteBatch).begin();
            verify(bitmapFontData, times(2)).setScale(anyFloat());
            verify(bitmapFont)
                .draw(eq(spriteBatch), any(GlyphLayout.class), anyFloat(), anyFloat());
            verify(spriteBatch).end();
        }
    }
}
