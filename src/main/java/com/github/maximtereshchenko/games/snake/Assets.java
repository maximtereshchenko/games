package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Set;

final class Assets {

    static final AssetDescriptor<Skin> SKIN =
        new AssetDescriptor<>("skin.json", Skin.class);
    static final AssetDescriptor<BitmapFont> BITMAP_FONT =
        new AssetDescriptor<>("default.fnt", BitmapFont.class, bitmapFontParameter());
    static final AssetDescriptor<I18NBundle> LOADING_BUNDLE =
        new AssetDescriptor<>("loading", I18NBundle.class);
    static final AssetDescriptor<I18NBundle> GAME_BUNDLE =
        new AssetDescriptor<>("game", I18NBundle.class);
    static final Set<AssetDescriptor<?>> LOADING_ASSETS = Set.of(SKIN, LOADING_BUNDLE);
    static final Set<AssetDescriptor<?>> GAME_ASSETS = Set.of(GAME_BUNDLE, BITMAP_FONT);

    private static BitmapFontLoader.BitmapFontParameter bitmapFontParameter() {
        var bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "skin.atlas";
        return bitmapFontParameter;
    }
}
