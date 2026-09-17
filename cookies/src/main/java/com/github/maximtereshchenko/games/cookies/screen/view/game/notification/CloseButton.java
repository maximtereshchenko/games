package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class CloseButton extends TextButton {

    CloseButton(AssetManager assetManager, Assets assets) {
        var gameAssets = assets.game();
        super(
            "X",
            assetManager.get(gameAssets.skin()),
            "close"
        );
        pad(8);
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
