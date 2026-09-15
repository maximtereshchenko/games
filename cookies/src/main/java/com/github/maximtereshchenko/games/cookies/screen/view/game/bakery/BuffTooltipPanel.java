package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpSeparator;

final class BuffTooltipPanel extends Table {

    BuffTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Buff buff
    ) {
        var style = skin.get(Style.class);
        pad(16);
        add(new Image(style.left))
            .right();
        add(
            new BuffNameLabel(
                skin,
                "buff-name-tooltip",
                bundle,
                buff
            )
        );
        add(new Image(style.right))
            .left()
            .row();
        add(new PopUpSeparator(skin))
            .colspan(3)
            .padTop(8)
            .padBottom(8)
            .growX()
            .row();
        add(
            new BuffDescriptionLabel(
                skin,
                "buff-description-tooltip",
                bundle,
                bakeryService,
                buff
            )
        )
            .colspan(3)
            .growX();
    }

    private static final class Style {

        Drawable left;
        Drawable right;
    }
}
