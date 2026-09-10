package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;

import java.math.BigDecimal;

final class BakingPowerStatisticsLabel extends BigDecimalStatisticsValueLabel {

    BakingPowerStatisticsLabel(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(skin, bigDecimalFormatter, bakeryService);
    }

    @Override
    BigDecimal bigDecimal(BakeryService bakeryService) {
        return bakeryService.bakingPower();
    }
}
