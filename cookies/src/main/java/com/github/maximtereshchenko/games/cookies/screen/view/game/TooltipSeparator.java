package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class TooltipSeparator extends Image {

    TooltipSeparator(Skin skin) {
        super(skin.get(Style.class).drawable);
    }

    private static final class Style {

        Drawable drawable;
    }
}
