package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.util.Random;

final class GoldenCookieOverlay extends WidgetGroup {

    GoldenCookieOverlay(Skin skin, BakeryService bakeryService, Random random) {
        setTouchable(Touchable.childrenOnly);
        addActor(
            new GoldenCookieButton(
                skin,
                bakeryService,
                random
            )
        );
    }
}
