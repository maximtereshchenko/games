package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

final class CookieIcon extends Image {

    CookieIcon(
        Skin skin,
        Random random,
        float parentWidth,
        float parentHeight
    ) {
        super(skin, "icon_cookie");
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
}
