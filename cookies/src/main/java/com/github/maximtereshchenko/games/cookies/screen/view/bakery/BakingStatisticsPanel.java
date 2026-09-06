package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class BakingStatisticsPanel extends Table {

    BakingStatisticsPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        background(skin.get(Style.class).background);
        defaults().pad(4);
        add(new CookieBalanceLabel(skin, bakeryService))
            .row();
        add(new CookiesLabel(skin, bundle)).row();
        add(new BakingRateLabel(skin, bundle, bakeryService))
            .row();
    }

    private static final class Style {

        Drawable background;
    }
}
