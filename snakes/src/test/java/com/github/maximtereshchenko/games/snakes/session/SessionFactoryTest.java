package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.snakes.configuration.Assets;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class SessionFactoryTest {

    private final ConfigurationReader configurationReader = mock();
    private final ShapeRenderer shapeRenderer = mock();
    private final SpriteBatch spriteBatch = mock();
    private final AssetManager assetManager = mock();
    private final Assets assets = mock();
    private final Viewport viewport = mock();
    private final Mode mode = mock();
    private final AssetDescriptor<BitmapFont> bitmapFontDescriptor = mock();
    private final AssetDescriptor<I18NBundle> gameBundleDescriptor = mock();
    private final SessionFactory sessionFactory = new SessionFactory(
        configurationReader,
        shapeRenderer,
        spriteBatch,
        assetManager,
        assets
    );

    @BeforeEach
    void setUp() {
        when(assets.bitmapFont()).thenReturn(bitmapFontDescriptor);
        when(assets.gameBundle()).thenReturn(gameBundleDescriptor);
        when(assetManager.get(bitmapFontDescriptor)).thenReturn(mock());
        when(assetManager.get(gameBundleDescriptor)).thenReturn(mock());
    }

    @Test
    void whenWorld_thenWorldWithEntities() {
        var component = new WorldDimensions(2, 3);
        when(mode.entities()).thenReturn("entities");
        when(configurationReader.value(eq("entities"), any()))
            .thenReturn(new Object[][]{new Object[]{component}});
        var registry = sessionFactory.registry(mode, viewport, viewport);
        Iterable<Entity> worldDimensionsEntities =
            registry.entities(new Query().all(WorldDimensions.class));
        assertThat(worldDimensionsEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldDimensions.class))
            .isSameAs(component);
    }

    @Test
    void whenWorld_thenDistinctConfiguredEntitiesCreated() {
        when(mode.entities()).thenReturn("entities");
        when(configurationReader.value(eq("entities"), any()))
            .thenReturn(
                new Object[][]{
                    new Object[]{new TurnTimer(1f, 0f)},
                    new Object[]{new WorldDimensions(2, 3)}
                }
            );
        var registry = sessionFactory.registry(mode, viewport, viewport);
        Iterable<Entity> turnTimerEntities =
            registry.entities(new Query().all(TurnTimer.class));
        Iterable<Entity> worldDimensionsEntities =
            registry.entities(new Query().all(WorldDimensions.class));
        assertThat(turnTimerEntities).hasSize(1);
        assertThat(worldDimensionsEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldDimensions.class))
            .usingRecursiveComparison()
            .isEqualTo(new WorldDimensions(2, 3));
    }
}
