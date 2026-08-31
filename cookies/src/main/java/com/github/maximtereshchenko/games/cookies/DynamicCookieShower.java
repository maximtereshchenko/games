package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

final class DynamicCookieShower extends WidgetGroup {

    private final Skin skin;
    private final Random random;

    DynamicCookieShower(Skin skin, Random random) {
        this.skin = skin;
        this.random = random;
        setLayoutEnabled(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (random.nextFloat() > 0.1f) {
            return;
        }
        var image = new Image(skin, "icon_cookie");
        image.setOrigin(Align.center);
        image.setScale(random.nextFloat(0.8f, 1.2f));
        image.setRotation(random.nextFloat(360));
        image.setPosition(random.nextFloat(getWidth()), getHeight());
        var duration = 2;
        image.addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.moveBy(random.nextFloat(-0.1f * getWidth(), 0.1f * getWidth()), -getHeight(), duration),
                    Actions.fadeOut(duration)
                ),
                Actions.removeActor()
            )
        );
        addActor(image);
    }
}
