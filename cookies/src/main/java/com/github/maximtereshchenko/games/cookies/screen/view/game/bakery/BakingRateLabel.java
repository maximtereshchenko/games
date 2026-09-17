package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.I18NBundle;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class BakingRateLabel extends Label {

    private final I18NBundle bundle;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;

    BakingRateLabel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(
            "",
            assetManager.get(assets.game().skin()),
            "baking-rate"
        );
        this.bundle = assetManager.get(assets.game().bundle());
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bundle.format(
                "bakery.baking-rate",
                bigDecimalFormatter.string(
                    bakeryService.bakingRate()
                )
            )
        );
    }
}
