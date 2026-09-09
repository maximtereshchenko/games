package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;

import java.math.BigDecimal;

final class CumulativeBakedStatisticsLabel extends BigDecimalStatisticsValueLabel {

    CumulativeBakedStatisticsLabel(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(skin, bigDecimalFormatter, bakeryService);
    }

    @Override
    BigDecimal bigDecimal(BakeryService bakeryService) {
        return bakeryService.cumulativeBaked();
    }

}
