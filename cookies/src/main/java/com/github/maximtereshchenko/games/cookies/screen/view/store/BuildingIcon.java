package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingIcon extends Stack {

    private final BakeryService bakeryService;
    private final Building building;
    private final Image disabled;
    private final Image enabled;
    private final float animationDurationSeconds;
    private boolean isLocked;

    BuildingIcon(
        Skin skin,
        String styleName,
        BakeryService bakeryService,
        Building building,
        float animationDurationSeconds
    ) {
        var style = skin.get(styleName, Style.class);
        this.bakeryService = bakeryService;
        this.building = building;
        this.disabled = new Image(style.disabled);
        this.enabled = new Image(style.enabled);
        this.animationDurationSeconds = animationDurationSeconds;
        this.isLocked = true;
        enabled.addAction(Actions.fadeOut(0));
        add(disabled);
        add(enabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isLocked && balanceGreaterThanTransactionValue()) {
            disabled.addAction(Actions.fadeOut(animationDurationSeconds));
            enabled.addAction(Actions.fadeIn(animationDurationSeconds));
            isLocked = false;
        }
    }

    private boolean balanceGreaterThanTransactionValue() {
        return bakeryService.balance()
                   .compareTo(
                       bakeryService.transactionValue(building)
                   ) >= 0;
    }

    private static final class Style {

        Drawable enabled;
        Drawable disabled;
    }
}
