package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BuildingIcon extends Stack {

    BuildingIcon(
        AssetManager assetManager,
        Assets assets,
        String styleName,
        BakeryService bakeryService,
        Building building,
        float animationDurationSeconds
    ) {
        var style = assetManager.get(assets.game().skin()).get(styleName, Style.class);
        var disabled = new Image(style.locked);
        var enabled = new Image(style.unlocked);
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

        Drawable unlocked;
        Drawable locked;
    }
}
