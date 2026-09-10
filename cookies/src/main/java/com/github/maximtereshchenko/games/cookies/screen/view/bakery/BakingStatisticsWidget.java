package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;

final class BakingStatisticsWidget extends Table {

    BakingStatisticsWidget(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        add()
            .height(Value.percentHeight(0.1f, this))
            .row();
        add(
            new BakingStatisticsPanel(
                skin,
                bundle,
                bigDecimalFormatter,
                bakeryService
            )
        )
            .growX();
        top();
    }
}
