package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.List;

final class Assets {

    static final AssetDescriptor<Skin> SKIN =
        new AssetDescriptor<>("skin.json", Skin.class);
    static final AssetDescriptor<I18NBundle> GAME_BUNDLE =
        new AssetDescriptor<>("game", I18NBundle.class);
    static final AssetDescriptor<I18NBundle> LOADING_BUNDLE =
        new AssetDescriptor<>("loading", I18NBundle.class);
    static final List<AssetDescriptor<?>> ALL = List.of(SKIN, GAME_BUNDLE, LOADING_BUNDLE);
}
