package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.BuffExtendedEffect;
import com.github.maximtereshchenko.games.cookies.domain.GoldenCookieEffect;

final class GoldenCookieNotification extends Table {

    GoldenCookieNotification(
        Skin skin,
        I18NBundle bundle,
        float x,
        float y,
        float parentWidth,
        float parentHeight,
        BakeryService bakeryService,
        GoldenCookieEffect goldenCookieEffect
    ) {
        defaults().pad(4);
        add(
            switch (goldenCookieEffect) {
                case BuffExtendedEffect buffExtendedEffect -> new BuffNameLabel(
                    skin,
                    "buff-name-notification",
                    bundle,
                    buffExtendedEffect.buff()
                );
            }
        )
            .row();
        add(
            switch (goldenCookieEffect) {
                case BuffExtendedEffect buffExtendedEffect -> new BuffDescriptionLabel(
                    skin,
                    "buff-description-notification",
                    bundle,
                    buffExtendedEffect.buff(),
                    bakeryService.buffDescription(
                        buffExtendedEffect.buff()
                    )
                );
            }
        );
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

    private static final class Style {

        Drawable background;
    }
}
