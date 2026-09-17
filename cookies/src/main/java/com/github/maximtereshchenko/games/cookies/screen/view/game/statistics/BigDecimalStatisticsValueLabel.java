package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.math.BigDecimal;

abstract class BigDecimalStatisticsValueLabel extends StatisticsValueLabel {

    private final BigDecimalFormatter bigDecimalFormatter;

    BigDecimalStatisticsValueLabel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(assetManager, assets, bakeryService);
        this.bigDecimalFormatter = bigDecimalFormatter;
    }

    @Override
    final String text(BakeryService bakeryService) {
        return bigDecimalFormatter.string(
            bigDecimal(bakeryService)
        );
    }

    abstract BigDecimal bigDecimal(BakeryService bakeryService);
}
