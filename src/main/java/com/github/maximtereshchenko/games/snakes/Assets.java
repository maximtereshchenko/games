package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Set;

public record Assets(
    AssetDescriptor<Skin> skin,
    AssetDescriptor<I18NBundle> loadingBundle,
    AssetDescriptor<BitmapFont> bitmapFont,
    AssetDescriptor<I18NBundle> gameBundle,
    AssetDescriptor<Music> music
) {

    public Set<AssetDescriptor<?>> gameAssets() {
        return Set.of(gameBundle, bitmapFont, music);
    }

    Set<AssetDescriptor<?>> loadingAssets() {
        return Set.of(skin, loadingBundle);
    }
}
