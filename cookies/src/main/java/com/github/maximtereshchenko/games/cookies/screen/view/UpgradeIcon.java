package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

public final class UpgradeIcon extends Image {

    public UpgradeIcon(Skin skin, Upgrade upgrade) {
        super(skin.get(upgrade.name(), Style.class).drawable);
    }

    private static final class Style {

        Drawable drawable;
    }
}
