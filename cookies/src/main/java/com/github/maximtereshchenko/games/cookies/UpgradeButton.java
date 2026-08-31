package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

final class UpgradeButton extends Container<ImageButton> {

    UpgradeButton(Skin skin) {
        super(new ImageButton(skin, "button_upgrade"));
        background(skin.getDrawable("tile_black_transparent25"));
    }
}
