package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class PlayBallSoundCollisionSystem extends CollisionSystem {

    private final Configuration configuration;
    private final AssetManager assetManager;

    PlayBallSoundCollisionSystem(
        Registry registry,
        Configuration configuration,
        AssetManager assetManager
    ) {
        super(
            registry,
            new Query().all(Ball.class),
            new Query()
        );
        this.configuration = configuration;
        this.assetManager = assetManager;
    }

    @Override
    void onCollision(
        RegistryEdit registryEdit,
        Entity colliderEntity,
        Entity impactedEntity
    ) {
        assetManager.get(
                configuration.assets()
                    .ballSound()
            )
            .play();
    }
}
