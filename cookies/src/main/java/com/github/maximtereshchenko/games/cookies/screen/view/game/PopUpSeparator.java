package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public final class PopUpSeparator extends Image {

    public PopUpSeparator(Skin skin) {
        super(skin.get(Style.class).drawable);
    }

    private static final class Style {

        Drawable drawable;
    }
}
