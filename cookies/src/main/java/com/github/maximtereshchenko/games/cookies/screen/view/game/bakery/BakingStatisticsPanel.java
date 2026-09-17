package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class BakingStatisticsPanel extends Table {

    BakingStatisticsPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        background(assetManager.get(assets.game().skin()).get(Style.class).background);
        defaults().pad(4);
        add(
            new CookieBalanceLabel(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService
            )
        )
            .row();
        add(new CookiesLabel(assetManager, assets)).row();
        add(
            new BakingRateLabel(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService
            )
        )
            .row();
    }

    private static final class Style {

        Drawable background;
    }
}
