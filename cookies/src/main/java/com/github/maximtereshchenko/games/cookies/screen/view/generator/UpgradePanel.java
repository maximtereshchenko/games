package com.github.maximtereshchenko.games.cookies.screen.view.generator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

final class UpgradePanel extends Container<Table> {

    private boolean isExpanded = false;

    UpgradePanel(Skin skin) {
        super(new Table());
        clip();
        for (var i = 0; i < 23; i++) {
            getActor().add(new UpgradeButton(skin));
            if (i % 5 == 4) {
                getActor().row();
            }
        }
        setTransform(true);
        addListener(eventListener());
    }

    @Override
    public float getPrefHeight() {
        if (isExpanded) {
            return super.getPrefHeight();
        }
        return getActor().getRowPrefHeight(0);
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
