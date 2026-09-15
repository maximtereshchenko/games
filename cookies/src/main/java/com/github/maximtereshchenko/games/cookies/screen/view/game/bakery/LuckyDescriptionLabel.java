package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.LuckyGoldenCookieEffect;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class LuckyDescriptionLabel extends Label {

    LuckyDescriptionLabel(
        Skin skin,
        String style,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        LuckyGoldenCookieEffect luckyGoldenCookieEffect
    ) {
        super(
            bundle.format(
                "golden-cookie.effect.lucky.description",
                bigDecimalFormatter.string(
                    luckyGoldenCookieEffect.amount()
                )
            ),
            skin,
            style
        );
        setAlignment(Align.center);
    }
}
