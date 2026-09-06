package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingIcon extends Stack {

    BuildingIcon(
        Skin skin,
        String styleName,
        BakeryService bakeryService,
        Building building,
        float animationDurationSeconds
    ) {
        var style = skin.get(styleName, Style.class);
        var disabled = new Image(style.disabled);
        var enabled = new Image(style.enabled);
        enabled.addAction(Actions.fadeOut(0));
        addAction(
            new UnlockBuildingAction(
                bakeryService,
                building,
                Actions.parallel(
                    Actions.addAction(Actions.fadeOut(animationDurationSeconds), disabled),
                    Actions.addAction(Actions.fadeIn(animationDurationSeconds), enabled)
                )
            )
        );
        add(disabled);
        add(enabled);
    }

    private static final class Style {

        Drawable enabled;
        Drawable disabled;
    }
}
