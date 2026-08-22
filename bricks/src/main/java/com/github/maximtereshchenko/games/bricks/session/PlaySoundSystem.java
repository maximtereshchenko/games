package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;
import com.github.maximtereshchenko.games.ecs.System;

final class PlaySoundSystem implements System {

    private final Iterable<Entity> entities;
    private final AssetDescriptor<Sound> assetDescriptor;
    private final AssetManager assetManager;
    private final UserProfile userProfile;

    PlaySoundSystem(
        Iterable<Entity> entities,
        AssetDescriptor<Sound> assetDescriptor,
        AssetManager assetManager,
        UserProfile userProfile
    ) {
        this.entities = entities;
        this.assetDescriptor = assetDescriptor;
        this.assetManager = assetManager;
        this.userProfile = userProfile;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (entities.iterator().hasNext()) {
            assetManager.get(assetDescriptor)
                .play(userProfile.soundVolume());
        }
    }
}
