package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class SnakeSessionFactoryTest {

    private final Dominion dominion = mock();
    private final Viewport viewport = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final SpriteBatch spriteBatch = mock();
    private final AssetManager assetManager = mock();
    private final SnakeSessionFactory snakeSessionFactory = new SnakeSessionFactory(
        shapeRenderer,
        spriteBatch,
        assetManager
    );

    @Test
    void whenDominion_thenDominionWithEntities() {
        try (var dominionStatic = mockStatic(Dominion.class)) {
            dominionStatic.when(Dominion::create).thenReturn(dominion);
            assertThat(snakeSessionFactory.dominion(new WorldDimensions(0, 0))).isEqualTo(dominion);
            verify(dominion, atLeastOnce()).createEntity(any());
        }
    }

    @Test
    void whenSystems_thenSystemsCreated() {
        assertThat(
            snakeSessionFactory.systems(
                dominion,
                Mode.CLASSIC,
                viewport,
                viewport,
                Map.of()
            )
        )
            .isNotEmpty();
    }
}