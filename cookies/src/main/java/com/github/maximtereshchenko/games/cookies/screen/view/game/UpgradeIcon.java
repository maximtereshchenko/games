package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

public final class UpgradeIcon extends Image {

    public UpgradeIcon(AssetManager assetManager, Assets assets, Upgrade upgrade) {
        super(assetManager.get(assets.game().skin()).get(upgrade.name(), Style.class).drawable);
    }

    private static final class Style {

        Drawable drawable;
    }
}
