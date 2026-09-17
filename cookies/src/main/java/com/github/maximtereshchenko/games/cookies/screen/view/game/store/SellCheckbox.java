package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class SellCheckbox extends TransactionConfigurationCheckbox {

    SellCheckbox(AssetManager assetManager, Assets assets) {
        super(assetManager, assets, assetManager.get(assets.game().bundle()).get("store.transaction.sell"));
    }
}
