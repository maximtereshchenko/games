package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Set;

record Assets(
    AssetDescriptor<Skin> skin,
    AssetDescriptor<I18NBundle> loadingBundle,
    AssetDescriptor<BitmapFont> bitmapFont,
    AssetDescriptor<I18NBundle> gameBundle
) {

    Set<AssetDescriptor<?>> loadingAssets() {
        return Set.of(skin, loadingBundle);
    }

    Set<AssetDescriptor<?>> gameAssets() {
        return Set.of(gameBundle, bitmapFont);
    }
}
