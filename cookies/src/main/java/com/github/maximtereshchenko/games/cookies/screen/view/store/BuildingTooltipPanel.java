package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingTooltipPanel extends Table {

    BuildingTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Building building
    ) {
        defaults().padBottom(8);
        add(
            new BuildingIcon(
                skin,
                "%s-tooltip".formatted(building.name()),
                bakeryService,
                building,
                0
            )
        )
            .width(Value.prefWidth);
        add(
            new BuildingTooltipHeader(
                skin,
                bundle,
                bakeryService,
                building
            )
        )
            .growX()
            .row();
        add(new Image(skin.get(Style.class).separator))
            .colspan(2)
            .growX()
            .padTop(8)
            .padBottom(16)
            .row();
        add(
            new BuildingFlavorTextLabel(
                skin,
                bundle,
                bakeryService,
                building
            )
        )
            .colspan(2)
            .right();
    }

    private static final class Style {

        Drawable separator;
    }
}
