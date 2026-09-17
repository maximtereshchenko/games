package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

abstract class BaseCookieBalanceLabel extends Label {

    BaseCookieBalanceLabel(String text, AssetManager assetManager, Assets assets) {
        super(text, assetManager.get(assets.game().skin()), "cookie-balance");
    }
}
