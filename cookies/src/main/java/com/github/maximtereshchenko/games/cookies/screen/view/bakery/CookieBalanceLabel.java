package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;

final class CookieBalanceLabel extends BaseCookieBalanceLabel {

    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;

    CookieBalanceLabel(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super("", skin);
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bigDecimalFormatter.string(
                bakeryService.balance()
            )
        );
    }
}
