package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class BakingStatisticsWidget extends Table {

    BakingStatisticsWidget(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        add()
            .height(Value.percentHeight(0.1f, this))
            .row();
        add(
            new BakingStatisticsPanel(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService
            )
        )
            .growX();
        top();
    }
}
