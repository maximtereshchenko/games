package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

class TransactionConfigurationCheckbox extends CheckBox {

    TransactionConfigurationCheckbox(AssetManager assetManager, Assets assets, String text) {
        super(text, assetManager.get(assets.game().skin()), "transaction-configuration");
    }
}
