package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

final class CookieParticle extends Image {

    CookieParticle(Skin skin, Random random, float x, float y) {
        super(skin, "icon_cookie");
        setTouchable(Touchable.disabled);
        setSize(getPrefWidth(), getPrefHeight());
        setOrigin(Align.center);
        setScale(random.nextFloat(0.8f, 1.2f));
        setRotation(random.nextFloat(360));
        setPosition(x - getWidth() / 2f, y - getHeight() / 2f);
        var duration = 1;
        addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.moveBy(
                        0,
                        -getHeight() * random.nextFloat(1, 3),
                        duration,
                        Interpolation.swingIn
                    ),
                    Actions.moveBy(
                        getWidth() * random.nextFloat(-1.5f, 1.5f),
                        0,
                        duration
                    ),
                    Actions.fadeOut(duration)
                ),
                Actions.removeActor()
            )
        );
    }
}
