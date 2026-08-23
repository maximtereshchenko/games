package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.screen.view.Indicator;
import com.github.maximtereshchenko.games.event.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class SessionFactoryTest {

    private final Configuration configuration = mock();
    private final Configuration.Assets assets = mock();
    private final EventBus<Event> eventBus = mock();
    private final AssetManager assetManager = mock();
    private final UserProfile userProfile = mock();
    private final Viewport viewport = mock();
    private final Indicator livesIndicator = mock();
    private final Indicator starsIndicator = mock();
    private final World world = mock();
    private final PhysicsObjectFactory physicsObjectFactory = mock();
    private final Input input = mock();
    private final AssetDescriptor<Sound> bonusSound = mock();
    private final AssetDescriptor<Sound> loseSound = mock();
    private final Fixture boundariesFixture = mock();
    private final SessionFactory sessionFactory = new SessionFactory(
        configuration,
        eventBus,
        physicsObjectFactory,
        assetManager,
        userProfile
    );

    @BeforeEach
    void setUp() {
        Gdx.input = input;
        when(configuration.assets()).thenReturn(assets);
        when(configuration.worldDimensions()).thenReturn(new Configuration.Dimensions(10, 10));
        when(assets.bonusSound()).thenReturn(bonusSound);
        when(assets.loseSound()).thenReturn(loseSound);
        when(configuration.maxStars()).thenReturn(3);
        when(physicsObjectFactory.boundariesFixture(world)).thenReturn(boundariesFixture);
    }

    @Test
    void whenRegistryUpdated_thenNoException() {
        var blueprints = new Blueprints.Builder(
            Map.of(BricksBlueprints.PADDLE, List.of(Paddle.INSTANCE))
        ).build();
        var registry = sessionFactory.registry(
            viewport,
            livesIndicator,
            starsIndicator,
            blueprints,
            List.of(),
            world,
            "easy",
            0
        );
        assertThatCode(() -> registry.update(0)).doesNotThrowAnyException();
    }
}
