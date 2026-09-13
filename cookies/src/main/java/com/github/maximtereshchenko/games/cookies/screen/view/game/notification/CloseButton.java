package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

final class CloseButton extends TextButton {

    CloseButton(Skin skin) {
        super("X", skin, "close");
        pad(8);
    }
}
