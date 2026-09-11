package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

class TransactionConfigurationCheckbox extends CheckBox {

    TransactionConfigurationCheckbox(Skin skin, String text) {
        super(text, skin, "transaction-configuration");
    }
}
