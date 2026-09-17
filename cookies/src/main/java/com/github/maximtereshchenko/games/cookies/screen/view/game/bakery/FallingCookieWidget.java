package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.Random;

final class FallingCookieWidget extends Image {

    FallingCookieWidget(
        AssetManager assetManager,
        Assets assets,
        Random random,
        float parentWidth,
        float parentHeight
    ) {
        super(assetManager.get(assets.game().skin()).get(Style.class).drawable);
        setOrigin(Align.center);
        setScale(random.nextFloat(0.8f, 1.2f));
        setRotation(random.nextFloat(360));
        setPosition(random.nextFloat(parentWidth), parentHeight);
        var duration = 2;
        addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.moveBy(
                        random.nextFloat(-0.1f, 0.1f) * parentWidth,
                        -parentHeight,
                        duration
                    ),
                    Actions.fadeOut(duration)
                ),
                Actions.removeActor()
            )
        );
    }

    private static final class Style {

        Drawable drawable;
    }
}
