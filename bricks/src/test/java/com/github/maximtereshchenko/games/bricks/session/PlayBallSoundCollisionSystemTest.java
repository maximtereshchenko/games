package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

final class PlayBallSoundCollisionSystemTest {

    private final Registry registry = new Registry();
    private final Configuration configuration = mock();
    private final Configuration.Assets assets = mock();
    private final AssetManager assetManager = mock();
    private final UserProfile userProfile = mock();
    private final AssetDescriptor<Sound> ballSound = mock();
    private final Sound sound = mock();
    private final PlayBallSoundCollisionSystem playBallSoundCollisionSystem =
        new PlayBallSoundCollisionSystem(
            registry,
            configuration,
            assetManager,
            userProfile
        );

    @Test
    void givenBallCollision_thenBallSoundPlayed() {
        when(configuration.assets()).thenReturn(assets);
        when(assets.ballSound()).thenReturn(ballSound);
        when(assetManager.get(ballSound)).thenReturn(sound);
        when(userProfile.soundVolume()).thenReturn(0.6f);
        registry.addSystems(playBallSoundCollisionSystem);
        var ballId = registry.createEntity();
        var otherId = registry.createEntity();
        registry.addComponents(ballId, Ball.INSTANCE, new Collisions(Set.of(otherId)));
        registry.addComponents(otherId, Paddle.INSTANCE, new Collisions(Set.of(ballId)));
        registry.update(0);
        verify(sound).play(0.6f);
    }
}
