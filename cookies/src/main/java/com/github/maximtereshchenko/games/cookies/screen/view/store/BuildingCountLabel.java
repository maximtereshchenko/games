package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.BuildingCountUpdated;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BuildingCountLabel extends Label implements Subscriber<Event> {

    private final I18NBundle bundle;
    private final String zeroValueKey;
    private final String valueKey;
    private final Building building;

    BuildingCountLabel(
        Skin skin,
        String styleName,
        I18NBundle bundle,
        String zeroValueKey,
        String valueKey,
        Building building,
        EventBus<Event> eventBus
    ) {
        super("", skin, styleName);
        this.bundle = bundle;
        this.zeroValueKey = zeroValueKey;
        this.valueKey = valueKey;
        this.building = building;
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof BuildingCountUpdated buildingCountUpdated &&
            buildingCountUpdated.building() == building
        ) {
            var count = buildingCountUpdated.count();
            setText(bundle.format(key(count), count));
        }
    }

    private String key(int count) {
        if (count == 0) {
            return zeroValueKey;
        }
        return valueKey;
    }
}
