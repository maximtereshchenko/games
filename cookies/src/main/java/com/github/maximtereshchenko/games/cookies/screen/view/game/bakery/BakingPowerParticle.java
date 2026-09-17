package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.Random;

final class BakingPowerParticle extends Label {

    BakingPowerParticle(
        AssetManager assetManager,
        Assets assets,
        Random random,
        float x,
        float y,
        String text
    ) {
        super(
            text,
            assetManager.get(assets.game().skin()),
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
