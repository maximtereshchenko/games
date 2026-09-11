package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingTooltipIcon extends Image {

    BuildingTooltipIcon(
        Skin skin,
        BakeryService bakeryService,
        Building building
    ) {
        var style = skin.get(building.name(), Style.class);
        super(style.disabled);
        addAction(
            new UnlockBuildingAction(
                bakeryService,
                building,
                Actions.sequence(
                    Actions.run(() -> setDrawable(style.enabled))
                )
            )
        );
    }

    private static final class Style {

        Drawable enabled;
        Drawable disabled;
    }
}
