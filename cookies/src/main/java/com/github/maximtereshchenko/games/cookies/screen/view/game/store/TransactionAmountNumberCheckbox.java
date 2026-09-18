package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class TransactionAmountNumberCheckbox extends TransactionAmountCheckbox {

    TransactionAmountNumberCheckbox(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        int amount
    ) {
        super(
            assetManager,
            assets,
            bakeryService,
            String.valueOf(amount)
        );
    }

    @Override
    public int value() {
        return Integer.parseInt(getText().toString());
    }
}
