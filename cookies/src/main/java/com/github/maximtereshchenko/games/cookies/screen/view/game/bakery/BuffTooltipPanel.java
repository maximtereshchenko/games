package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpSeparator;

final class BuffTooltipPanel extends Table {

    BuffTooltipPanel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Buff buff
    ) {
        var style = assetManager.get(assets.game().skin()).get(Style.class);
        pad(16);
        add(new Image(style.left))
            .right();
        add(
            new BuffNameLabel(
                assetManager,
                assets,
                "buff-name-tooltip",
                bakeryService,
                buff
            )
        );
        add(new Image(style.right))
            .left()
            .row();
        add(new PopUpSeparator(assetManager, assets))
            .colspan(3)
            .padTop(8)
            .padBottom(8)
            .growX()
            .row();
        add(
            new BuffDescriptionLabel(
                assetManager,
                assets,
                "buff-description-tooltip",
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
