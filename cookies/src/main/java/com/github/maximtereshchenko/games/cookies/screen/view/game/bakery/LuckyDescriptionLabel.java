package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.domain.LuckyGoldenCookieEffect;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class LuckyDescriptionLabel extends Label {

    LuckyDescriptionLabel(
        AssetManager assetManager,
        Assets assets,
        String style,
        BigDecimalFormatter bigDecimalFormatter,
        LuckyGoldenCookieEffect luckyGoldenCookieEffect
    ) {
        super(
            assetManager.get(assets.game().bundle()).format(
                "golden-cookie.effect.lucky.description",
                bigDecimalFormatter.string(
                    luckyGoldenCookieEffect.amount()
                )
            ),
            assetManager.get(assets.game().skin()),
            style
        );
        setAlignment(Align.center);
    }
}
