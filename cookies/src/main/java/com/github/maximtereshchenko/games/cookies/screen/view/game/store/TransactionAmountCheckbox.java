package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

abstract class TransactionAmountCheckbox extends TransactionConfigurationCheckbox {

    TransactionAmountCheckbox(AssetManager assetManager, Assets assets, String text) {
        super(assetManager, assets, text);
    }

    abstract int value();
}
