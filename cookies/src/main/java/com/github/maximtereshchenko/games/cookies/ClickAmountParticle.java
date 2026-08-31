package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Random;

final class ClickAmountParticle extends Label {

    ClickAmountParticle(Skin skin, Random random, float x, float y) {
        super("+1", skin, "label_clickAmount");
        setTouchable(Touchable.disabled);
        setPosition(
            x - getWidth() * random.nextFloat(),
            y - getHeight() / 2f
        );
        var duration = 5;
        addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.moveBy(
                        0,
                        getHeight() * 20,
                        duration
                    ),
                    Actions.fadeOut(duration)
                ),
                Actions.removeActor()
            )
        );
    }
}
