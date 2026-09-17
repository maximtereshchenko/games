package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class CookiesLabel extends BaseCookieBalanceLabel {

    CookiesLabel(AssetManager assetManager, Assets assets) {
        super(
            assetManager.get(assets.game().bundle()).get("bakery.cookies.unit"),
            assetManager,
            assets
        );
    }
}
