package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.BuildingUnlocked;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BuildingIcon extends Stack implements Subscriber<Event> {

    private final Image disabled;
    private final Image enabled;
    private final float animationDurationSeconds;
    private final Building building;

    BuildingIcon(
        Skin skin,
        String styleName,
        float animationDurationSeconds,
        Building building,
        EventBus<Event> eventBus
    ) {
        var style = skin.get(styleName, Style.class);
        this.disabled = new Image(style.disabled);
        this.enabled = new Image(style.enabled);
        this.animationDurationSeconds = animationDurationSeconds;
        this.building = building;
        enabled.addAction(Actions.fadeOut(0));
        add(disabled);
        add(enabled);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof BuildingUnlocked buildingUnlocked &&
            buildingUnlocked.building() == building
        ) {
            disabled.addAction(Actions.fadeOut(animationDurationSeconds));
            enabled.addAction(Actions.fadeIn(animationDurationSeconds));
        }
    }

    private static final class Style {

        Drawable enabled;
        Drawable disabled;
    }
}
