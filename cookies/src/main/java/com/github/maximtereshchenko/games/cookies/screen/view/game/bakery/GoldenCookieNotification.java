package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.BuffResetEffect;
import com.github.maximtereshchenko.games.cookies.domain.GoldenCookieEffect;
import com.github.maximtereshchenko.games.cookies.domain.LuckyGoldenCookieEffect;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class GoldenCookieNotification extends Table {

    GoldenCookieNotification(
        Skin skin,
        I18NBundle bundle,
        float x,
        float y,
        float parentWidth,
        float parentHeight,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        GoldenCookieEffect goldenCookieEffect
    ) {
        var effectNameStyle = "golden-cookie-effect-name";
        var effectDescriptionStyle = "golden-cookie-effect-description";
        defaults().pad(4);
        add(
            switch (goldenCookieEffect) {
                case BuffResetEffect buffExtendedEffect -> new BuffNameLabel(
                    skin,
                    effectNameStyle,
                    bundle,
                    buffExtendedEffect.buff()
                );
                case LuckyGoldenCookieEffect _ -> new Label(
                    bundle.get("golden-cookie.effect.lucky.name"),
                    skin,
                    effectNameStyle
                );
            }
        )
            .row();
        add(
            switch (goldenCookieEffect) {
                case BuffResetEffect buffResetEffect -> new BuffDescriptionLabel(
                    skin,
                    effectDescriptionStyle,
                    bundle,
                    bakeryService,
                    buffResetEffect.buff()
                );
                case LuckyGoldenCookieEffect luckyGoldenCookieEffect -> new LuckyDescriptionLabel(
                    skin,
                    effectDescriptionStyle,
                    bundle,
                    bigDecimalFormatter,
                    luckyGoldenCookieEffect
                );
            }
        )
            .growX();
        background(skin.get(Style.class).background);
        pack();
        var halfWidth = getWidth() / 2;
        var halfHeight = getHeight() / 2;
        setPosition(
            Math.clamp(x, halfWidth, parentWidth - halfWidth) - halfWidth,
            Math.clamp(y, halfHeight, parentHeight - halfHeight) - halfHeight
        );
        var entranceDuration = 0.4f;
        addAction(
            Actions.sequence(
                Actions.fadeOut(0),
                Actions.parallel(
                    Actions.moveTo(
                        getX(),
                        Math.min(getY() + getHeight(), parentHeight - getHeight()),
                        entranceDuration
                    ),
                    Actions.fadeIn(
                        entranceDuration,
                        Interpolation.circleIn
                    )
                ),
                Actions.delay(2),
                Actions.fadeOut(0.5f),
                Actions.removeActor()
            )
        );
    }

    @Override
    public float getPrefWidth() {
        return 350;
    }

    private static final class Style {

        Drawable background;
    }
}
