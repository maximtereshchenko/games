package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

final class SellCheckbox extends TransactionConfigurationCheckbox {

    SellCheckbox(Skin skin, I18NBundle bundle) {
        super(skin, bundle.get("store.transaction.sell"));
    }
}
