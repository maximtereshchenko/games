package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

abstract class TransactionAmountCheckbox extends TransactionConfigurationCheckbox {

    TransactionAmountCheckbox(Skin skin, String text) {
        super(skin, text);
    }

    abstract int value();
}
