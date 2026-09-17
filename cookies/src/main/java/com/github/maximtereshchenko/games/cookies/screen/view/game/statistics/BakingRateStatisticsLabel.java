package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.math.BigDecimal;

final class BakingRateStatisticsLabel extends BigDecimalStatisticsValueLabel {

    BakingRateStatisticsLabel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(assetManager, assets, bigDecimalFormatter, bakeryService);
    }

    @Override
    BigDecimal bigDecimal(BakeryService bakeryService) {
        return bakeryService.bakingRate();
    }
}
