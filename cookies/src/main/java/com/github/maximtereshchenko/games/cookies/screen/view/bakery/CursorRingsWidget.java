package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.BuildingCountUpdated;
import com.github.maximtereshchenko.games.cookies.domain.Event;

import java.util.ArrayList;
import java.util.List;

final class CursorRingsWidget extends WidgetGroup implements Subscriber<Event> {

    private final Skin skin;
    private final List<CursorWidget> cursorWidgets;
    private float accumulatedTimeSeconds; //TODO

    CursorRingsWidget(Skin skin, EventBus<Event> eventBus) {
        this.skin = skin;
        this.cursorWidgets = new ArrayList<>();
        setLayoutEnabled(false);
        eventBus.subscribe(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        accumulatedTimeSeconds += delta;
        var cursorsPerRing = 50;
        for (var i = 0; i < cursorWidgets.size(); i++) {
            cursorWidgets.get(i)
                .transform(
                    i / cursorsPerRing,
                    i % cursorsPerRing,
                    cursorsPerRing,
                    360f / cursorsPerRing,
                    accumulatedTimeSeconds,
                    getOriginX(),
                    getOriginY()
                );
        }
    }

    @Override
    public void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
        for (var cursor : cursorWidgets) {
            cursor.scale(getWidth());
        }
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof BuildingCountUpdated buildingCountUpdated &&
            buildingCountUpdated.building() == Building.CURSOR
        ) {
            for (
                var i = cursorWidgets.size();
                i < buildingCountUpdated.count();
                i++
            ) {
                var cursorWidget = new CursorWidget(skin);
                cursorWidget.scale(getWidth());
                cursorWidgets.add(cursorWidget);
                addActor(cursorWidget);
            }
        }
    }
}