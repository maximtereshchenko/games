package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.Assets;
import com.github.maximtereshchenko.games.snakes.Configuration;
import com.github.maximtereshchenko.games.snakes.Mode;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class SnakeSessionFactoryTest {

    private final Configuration configuration = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final SpriteBatch spriteBatch = mock();
    private final AssetManager assetManager = mock();
    private final Assets assets = mock();
    private final Dominion dominion = mock();
    private final EntityFactory entityFactory = mock();
    private final Viewport viewport = mock();
    private final Mode mode = mock();
    private final SnakeSessionFactory snakeSessionFactory = new SnakeSessionFactory(
        configuration,
        shapeRenderer,
        spriteBatch,
        assetManager,
        assets
    );

    @Test
    void whenDominion_thenDominionWithEntities() {
        try (var dominionStatic = mockStatic(Dominion.class)) {
            dominionStatic.when(Dominion::create).thenReturn(dominion);
            when(configuration.snakeHeadPosition()).thenReturn(new Position(0, 1));
            when(configuration.snakeHeadForwardDirection()).thenReturn(Direction.UP);
            when(configuration.snakeLength()).thenReturn(2);
            var worldDimensions = new WorldDimensions(1, 2);
            assertThat(snakeSessionFactory.dominion(entityFactory, worldDimensions, mode))
                .isEqualTo(dominion);
            verify(entityFactory).createGlobals(dominion, worldDimensions);
            verify(entityFactory).createHead(dominion, mode);
            verify(entityFactory).createSegment(dominion, new Position(0, 0), 1);
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