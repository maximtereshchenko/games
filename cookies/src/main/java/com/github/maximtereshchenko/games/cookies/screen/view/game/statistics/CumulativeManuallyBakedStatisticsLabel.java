package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.math.BigDecimal;

final class CumulativeManuallyBakedStatisticsLabel extends BigDecimalStatisticsValueLabel {

    CumulativeManuallyBakedStatisticsLabel(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(skin, bigDecimalFormatter, bakeryService);
    }

    @Override
    BigDecimal bigDecimal(BakeryService bakeryService) {
        return bakeryService.cumulativeManuallyBaked();
    }
}
