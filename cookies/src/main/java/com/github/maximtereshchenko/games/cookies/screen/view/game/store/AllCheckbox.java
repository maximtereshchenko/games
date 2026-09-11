package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

final class AllCheckbox extends TransactionAmountCheckbox {

    AllCheckbox(Skin skin, I18NBundle bundle) {
        super(skin, bundle.get("store.transaction.all"));
        setVisible(false);
    }

    @Override
    public int value() {
        return Integer.MAX_VALUE;
    }
}
