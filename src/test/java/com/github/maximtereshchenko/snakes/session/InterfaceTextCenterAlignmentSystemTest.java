package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class InterfaceTextCenterAlignmentSystemTest {

    private final World world = new World();
    private final Iterable<Entity> centerAlignedEntities =
        world.entities(new Query().all(CenterAligned.class, InterfacePosition.class));
    private final Viewport viewport = mock();
    private final BitmapFont bitmapFont = mock();
    private final InterfaceTextCenterAlignmentSystem system =
        new InterfaceTextCenterAlignmentSystem(world, viewport, bitmapFont);

    @BeforeEach
    void setUp() {
        world.addSystems(system);
        when(viewport.getWorldWidth()).thenReturn(100f);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            CenterAligned.INSTANCE,
            new InterfaceText(1, "text"),
            new InterfacePosition(0, 10)
        );
        world.update(0);
        assertThat(centerAlignedEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfacePosition.class).x)
            .isEqualTo(0f);
    }

    @Test
    void givenTurnStartedEvent_thenInterfacePositionCentered() {
        world.addComponents(
            world.createEntity(),
            CenterAligned.INSTANCE,
            new InterfaceText(1, "text"),
            new InterfacePosition(0, 10)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        try (
            var _ = mockConstruction(
                GlyphLayout.class,
                (mock, _) -> mock.width = 20f
            )
        ) {
            world.update(0);
        }
        assertThat(centerAlignedEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfacePosition.class).x)
            .isEqualTo(40f);
    }
}
