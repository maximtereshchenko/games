package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.math.BigDecimal;
import java.util.Random;

final class BakingPowerParticle extends Label {

    BakingPowerParticle(
        Skin skin,
        Random random,
        float x,
        float y,
        BigDecimal bigDecimal
    ) {
        super(
            "+" + bigDecimal,
            skin,
            "baking-power"
        );
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
