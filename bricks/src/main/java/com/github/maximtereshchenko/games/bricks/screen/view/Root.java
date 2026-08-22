package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;

public abstract class Root extends Table {

    protected Root(
        Configuration configuration,
        AssetManager assetManager
    ) {
        setFillParent(true);
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
