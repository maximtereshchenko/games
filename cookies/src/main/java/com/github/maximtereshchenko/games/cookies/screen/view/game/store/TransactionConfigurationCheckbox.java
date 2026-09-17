package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

class TransactionConfigurationCheckbox extends CheckBox {

    TransactionConfigurationCheckbox(AssetManager assetManager, Assets assets, String text) {
        var gameAssets = assets.game();
        super(
            text,
            assetManager.get(assets.game().skin()),
            "transaction-configuration"
        );
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    assetManager.get(gameAssets.tickSound())
                        .play();
                }
            }
        );
    }
}
