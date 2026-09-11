package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

final class TransactionAmountNumberCheckbox extends TransactionAmountCheckbox {

    TransactionAmountNumberCheckbox(Skin skin, int amount) {
        super(skin, String.valueOf(amount));
    }

    @Override
    public int value() {
        return Integer.parseInt(getText().toString());
    }
}
