package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class TransactionAmountNumberCheckbox extends TransactionAmountCheckbox {

    TransactionAmountNumberCheckbox(AssetManager assetManager, Assets assets, int amount) {
        super(assetManager, assets, String.valueOf(amount));
    }

    @Override
    public int value() {
        return Integer.parseInt(getText().toString());
    }
}
