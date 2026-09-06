package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.math.RoundingMode;

final class CookieBalanceLabel extends BaseCookieBalanceLabel {

    private final BakeryService bakeryService;

    CookieBalanceLabel(Skin skin, BakeryService bakeryService) {
        super("", skin);
        this.bakeryService = bakeryService;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bakeryService.balance()
                .setScale(0, RoundingMode.FLOOR)
                .toString()
        );
    }
}
