package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;
import com.github.maximtereshchenko.games.ecs.System;

final class PlaySoundSystem implements System {

    private final Iterable<Entity> entities;
    private final AssetDescriptor<Sound> assetDescriptor;
    private final AssetManager assetManager;

    PlaySoundSystem(
        Iterable<Entity> entities,
        AssetDescriptor<Sound> assetDescriptor,
        AssetManager assetManager
    ) {
        this.entities = entities;
        this.assetDescriptor = assetDescriptor;
        this.assetManager = assetManager;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (entities.iterator().hasNext()) {
            assetManager.get(assetDescriptor).play();
        }
    }
}
