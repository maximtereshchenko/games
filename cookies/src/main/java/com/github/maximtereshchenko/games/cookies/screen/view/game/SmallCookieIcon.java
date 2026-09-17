package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

public final class SmallCookieIcon extends Image {

    public SmallCookieIcon(AssetManager assetManager, Assets assets) {
        super(assetManager.get(assets.game().skin()).get(Style.class).drawable);
    }

    private static final class Style {

        Drawable drawable;
    }
}
