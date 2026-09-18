package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BuyCheckbox extends TransactionConfigurationCheckbox {

    BuyCheckbox(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        AllCheckbox allCheckbox,
        TransactionAmountNumberCheckbox transactionAmountNumberCheckbox
    ) {
        super(
            assetManager,
            assets,
            bakeryService,
            assetManager.get(assets.game().bundle())
                .get("store.transaction.buy")
        );
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    allCheckbox.setVisible(!isChecked());
                    if (allCheckbox.isChecked()) {
                        allCheckbox.setChecked(false);
                        transactionAmountNumberCheckbox.setChecked(true);
                    }
                }
            }
        );
    }
}
