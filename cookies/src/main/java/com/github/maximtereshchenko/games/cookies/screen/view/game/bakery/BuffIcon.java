package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.Buff;

final class BuffIcon extends Image {

    BuffIcon(Skin skin, Buff buff) {
        super(skin.get(buff.name(), Style.class).drawable);
    }

    private static final class Style {

        Drawable drawable;
    }
}