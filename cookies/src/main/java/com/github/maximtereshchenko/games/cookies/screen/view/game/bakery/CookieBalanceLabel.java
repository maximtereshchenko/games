package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class CookieBalanceLabel extends BaseCookieBalanceLabel {

    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;

    CookieBalanceLabel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super("", assetManager, assets);
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
