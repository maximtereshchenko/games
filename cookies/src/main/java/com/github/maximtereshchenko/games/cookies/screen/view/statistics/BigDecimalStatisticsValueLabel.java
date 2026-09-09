package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;

import java.math.BigDecimal;

abstract class BigDecimalStatisticsValueLabel extends StatisticsValueLabel {

    private final BigDecimalFormatter bigDecimalFormatter;

    BigDecimalStatisticsValueLabel(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(skin, bakeryService);
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
