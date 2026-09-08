package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;

final class BakingRateLabel extends Label {

    private final I18NBundle bundle;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;

    BakingRateLabel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(
            "",
            skin,
            "baking-rate"
        );
        this.bundle = bundle;
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
