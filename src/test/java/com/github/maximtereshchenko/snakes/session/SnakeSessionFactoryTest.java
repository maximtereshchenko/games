package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.snakes.configuration.Assets;
import com.github.maximtereshchenko.snakes.configuration.ConfigurationReader;
import com.github.maximtereshchenko.snakes.configuration.Mode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class SnakeSessionFactoryTest {

    private final ConfigurationReader configurationReader = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final SpriteBatch spriteBatch = mock();
    private final AssetManager assetManager = mock();
    private final Assets assets = mock();
    private final EntityFactory entityFactory = mock();
    private final Viewport viewport = mock();
    private final Mode mode = mock();
    private final SnakeSessionFactory snakeSessionFactory =
        new SnakeSessionFactory(
            configurationReader,
            shapeRenderer,
            spriteBatch,
            assetManager,
            assets
        );

    @Test
    void whenWorld_thenWorldWithEntities() {
        var component = new Session(Session.Status.RUNNING);
        when(configurationReader.entities(mode))
            .thenReturn(new Object[][]{new Object[]{component}});
        var world = snakeSessionFactory.world(
            mode,
            entityFactory,
            viewport,
            viewport
        );
        assertThat(world.entities(new Query().all(Session.class)))
            .singleElement()
            .extracting(entity -> entity.component(Session.class))
            .isSameAs(component);
    }

    @Test
    void whenWorld_thenDistinctConfiguredEntitiesCreated() {
        when(configurationReader.entities(mode))
            .thenReturn(
                new Object[][]{
                    new Object[]{new Session(Session.Status.RUNNING)},
                    new Object[]{new WorldDimensions(2, 3)}
                }
            );
        var world = snakeSessionFactory.world(mode, entityFactory, viewport, viewport);
        assertThat(world.entities(new Query().all(Session.class))).hasSize(1);
        assertThat(world.entities(new Query().all(WorldDimensions.class)))
            .singleElement()
            .extracting(entity -> entity.component(WorldDimensions.class))
            .usingRecursiveComparison()
            .isEqualTo(new WorldDimensions(2, 3));
    }
}