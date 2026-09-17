package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class StoreLabel extends Label {

    StoreLabel(AssetManager assetManager, Assets assets) {
        super(
            assetManager.get(assets.game().bundle()).get("store.title"),
            assetManager.get(assets.game().skin()),
            "store"
        );
        setAlignment(Align.center);
    }
}
