package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BottomOverlayWidget extends Container<Image> {

    BottomOverlayWidget(AssetManager assetManager, Assets assets) {
        super(
            new Image(
                assetManager.get(assets.game().skin()).get(Style.class).drawable
            )
        );
        fill()
            .height(Value.percentHeight(0.6f, this))
            .bottom();
    }

    private static final class Style {

        Drawable drawable;
    }
}
