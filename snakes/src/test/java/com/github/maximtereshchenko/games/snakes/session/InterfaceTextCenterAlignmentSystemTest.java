package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class InterfaceTextCenterAlignmentSystemTest {

    private final World world = new World();
    private final Iterable<Entity> centerAlignedEntities =
        world.entities(new Query().all(CenterAligned.class, InterfacePosition.class));
    private final Viewport viewport = mock();
    private final BitmapFont bitmapFont = mock();
    private final ScaledFont scaledFont = mock();
    private final GlyphLayout glyphLayout = mock();
    private final InterfaceTextCenterAlignmentSystem system =
        new InterfaceTextCenterAlignmentSystem(
            world,
            viewport,
            scaledFont,
            glyphLayout
        );

    @BeforeEach
    void setUp() {
        world.addSystems(system);
        when(viewport.getWorldWidth()).thenReturn(100f);
        glyphLayout.width = 20f;
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
    void whenUpdated_thenInterfacePositionCentered() {
        world.addComponents(
            world.createEntity(),
            CenterAligned.INSTANCE,
            new InterfaceText(2, "text"),
            new InterfacePosition(0, 10)
        );
        world.update(0);
        verify(scaledFont).use(eq(2), any());
        verify(glyphLayout).setText(bitmapFont, "text");
        assertThat(centerAlignedEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfacePosition.class).x)
            .isEqualTo(40f);
    }
}
