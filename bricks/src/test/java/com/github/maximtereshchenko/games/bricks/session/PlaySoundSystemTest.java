package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class PlaySoundSystemTest {

    private final Registry registry = new Registry();
    private final AssetDescriptor<Sound> assetDescriptor = mock();
    private final AssetManager assetManager = mock();
    private final UserProfile userProfile = mock();
    private final Sound sound = mock();
    private final PlaySoundSystem playSoundSystem = new PlaySoundSystem(
        registry.view(new Query().all(Bonus.class, Activated.class)),
        assetDescriptor,
        assetManager,
        userProfile
    );

    @Test
    void givenMatchingEntities_thenSoundPlayed() {
        when(assetManager.get(assetDescriptor)).thenReturn(sound);
        when(userProfile.soundVolume()).thenReturn(0.3f);
        registry.addSystems(playSoundSystem);
        registry.addComponents(registry.createEntity(), Bonus.INSTANCE, Activated.INSTANCE);
        registry.update(0);
        verify(sound).play(0.3f);
    }

    @Test
    void givenNoMatchingEntities_thenSoundNotPlayed() {
        registry.addSystems(playSoundSystem);
        registry.update(0);
        verifyNoInteractions(assetManager);
    }
}
