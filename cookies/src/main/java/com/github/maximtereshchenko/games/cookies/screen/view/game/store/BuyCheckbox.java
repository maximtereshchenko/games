package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;

final class BuyCheckbox extends TransactionConfigurationCheckbox {

    BuyCheckbox(
        Skin skin,
        I18NBundle bundle,
        AllCheckbox allCheckbox,
        TransactionAmountNumberCheckbox transactionAmountNumberCheckbox
    ) {
        super(skin, bundle.get("store.transaction.buy"));
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
