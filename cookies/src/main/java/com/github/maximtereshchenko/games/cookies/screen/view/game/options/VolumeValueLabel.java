package com.github.maximtereshchenko.games.cookies.screen.view.game.options;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class VolumeValueLabel extends Label {

    private final I18NBundle bundle;

    VolumeValueLabel(
        AssetManager assetManager,
        Assets assets
    ) {
        var gameAssets = assets.game();
        super(
            "",
            assetManager.get(gameAssets.skin()),
            "volume"
        );
        this.bundle = assetManager.get(gameAssets.bundle());
    }

    void setValue(float value) {
        setText(
            bundle.format(
                "options.slider.volume.value",
                value
            )
        );
    }
}
