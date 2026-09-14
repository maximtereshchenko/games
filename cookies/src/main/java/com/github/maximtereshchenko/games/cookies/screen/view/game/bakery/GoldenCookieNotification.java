package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Buff;

final class GoldenCookieNotification extends Table {

    GoldenCookieNotification(
        Skin skin,
        I18NBundle bundle,
        float x,
        float y,
        Buff buff
    ) {
        defaults().pad(4);
        add(
            new Label(
                bundle.get("buff.%s.name".formatted(buff)),
                skin,
                "buff-name-notification"
            )
        )
            .row();
        add(
            new Label(
                bundle.get("buff.%s.description".formatted(buff)),
                skin,
                "buff-description-notification"
            )
        );
        background(skin.get(Style.class).background);
        pack();
        setPosition(
            Math.max(0, x - getWidth() / 2),
            Math.max(0, y - getHeight() / 2)
        );
        var entranceDuration = 0.4f;
        addAction(
            Actions.sequence(
                Actions.fadeOut(0),
                Actions.parallel(
                    Actions.moveBy(
                        0,
                        getHeight(),
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
