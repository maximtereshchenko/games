package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BuffIcon extends Image {

    BuffIcon(AssetManager assetManager, Assets assets, Buff buff) {
        super(assetManager.get(assets.game().skin()).get(buff.name(), Style.class).drawable);
    }

    private static final class Style {

        Drawable drawable;
    }
}