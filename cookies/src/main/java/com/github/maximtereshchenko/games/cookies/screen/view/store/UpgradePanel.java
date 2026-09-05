package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.domain.UpgradeUnlocked;

final class UpgradePanel extends Container<Table> implements Subscriber<Event> {

    private final Skin skin;
    private final I18NBundle bundle;
    private final EventBus<Event> eventBus;
    private boolean isExpanded = false;

    UpgradePanel(
        Skin skin,
        I18NBundle bundle,
        EventBus<Event> eventBus
    ) {
        super(new Table());
        this.skin = skin;
        this.bundle = bundle;
        this.eventBus = eventBus;
        clip();
        top();
        left();
        setTransform(true);
        addListener(eventListener());
        eventBus.subscribe(this);
    }

    @Override
    public float getPrefHeight() {
        var table = getActor();
        if (isExpanded || !table.hasChildren()) {
            return super.getPrefHeight();
        }
        return table.getRowPrefHeight(0);
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof UpgradeUnlocked upgradeUnlocked)) {
            return;
        }
        var table = getActor();
        table.add(
            new UpgradeButton(
                skin,
                bundle,
                upgradeUnlocked.upgrade(),
                eventBus
            )
        );
        if (table.getChildren().size % 5 == 0) {
            table.row();
        }
    }

    EventListener eventListener() {
        return new InputListener() {

            @Override
            public void enter(
                InputEvent event,
                float x,
                float y,
                int pointer,
                Actor fromActor
            ) {
                isExpanded = true;
                invalidateHierarchy();
            }

            @Override
            public void exit(
                InputEvent event,
                float x,
                float y,
                int pointer,
                Actor toActor
            ) {
                if (pointer == -1) {
                    isExpanded = false;
                    invalidateHierarchy();
                }
            }
        };
    }
}
