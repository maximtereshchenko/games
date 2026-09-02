package com.github.maximtereshchenko.games.cookies.screen.view.generator;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class UpgradeButton extends Container<ImageButton> {

    UpgradeButton(Skin skin) {
        var style = skin.get(Style.class);
        super(new ImageButton(style));
        background(style.background);
    }

    private static final class Style extends ImageButton.ImageButtonStyle {

        Drawable background;
    }
}
