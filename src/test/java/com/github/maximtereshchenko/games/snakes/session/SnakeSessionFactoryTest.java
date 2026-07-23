package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.configuration.Assets;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class SnakeSessionFactoryTest {

    private final ShapeRenderer shapeRenderer = mock();
    private final SpriteBatch spriteBatch = mock();
    private final AssetManager assetManager = mock();
    private final Assets assets = mock();
    private final Dominion dominion = mock();
    private final EntityFactory entityFactory = mock();
    private final Viewport viewport = mock();
    private final Mode mode = mock();
    private final SnakeSessionFactory snakeSessionFactory = new SnakeSessionFactory(
        shapeRenderer,
        spriteBatch,
        assetManager,
        assets
    );

    @Test
    void whenDominion_thenDominionWithEntities() {
        try (var dominionStatic = mockStatic(Dominion.class)) {
            dominionStatic.when(Dominion::create).thenReturn(dominion);
            var components = new Object[]{new Object()};
            when(mode.entities()).thenReturn(List.<Object[]>of(components));
            assertThat(snakeSessionFactory.dominion(mode))
                .isEqualTo(dominion);
            verify(dominion).createEntity(components);
        }
    }

    @Test
    void whenSystems_thenSystemsCreated() {
        assertThat(
            snakeSessionFactory.systems(
                dominion,
                entityFactory,
                mode,
                viewport,
                viewport
            )
        )
            .isNotEmpty();
    }
}