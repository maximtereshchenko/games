package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.domain.GeneratorUnlocked;

final class BuildingIcon extends Stack implements Subscriber<Event> {

    private final Image disabled;
    private final Image enabled;
    private final Building building;

    BuildingIcon(
        Skin skin,
        Building building,
        EventBus<Event> eventBus
    ) {
        var style = skin.get(building.name(), Style.class);
        this.disabled = new Image(style.disabled);
        this.enabled = new Image(style.enabled);
        this.building = building;
        enabled.addAction(Actions.fadeOut(0));
        add(disabled);
        add(enabled);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(building)
        ) {
            var duration = 0.5f;
            disabled.addAction(Actions.fadeOut(duration));
            enabled.addAction(Actions.fadeIn(duration));
        }
    }

    private static final class Style {

        Drawable enabled;
        Drawable disabled;
    }
}
