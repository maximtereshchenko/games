package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class CloseButton extends TextButton {

    CloseButton(AssetManager assetManager, Assets assets) {
        super("X", assetManager.get(assets.game().skin()), "close");
        pad(8);
    }
}
