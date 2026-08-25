package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;

public abstract class Root extends ScreenLayout {

    protected Root(
        Configuration configuration,
        AssetManager assetManager
    ) {
        var userInterface = configuration.background()
            .userInterface();
        background(
            new TextureRegionDrawable(
                assetManager.get(
                        configuration.assets()
                            .textureAtlas()
                    )
                    .findRegion(userInterface.texture())
            )
                .tint(userInterface.color())
        );
    }
}
