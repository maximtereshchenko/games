package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

import java.util.HashSet;
import java.util.Set;

final class UpgradePanel extends Container<Table> {

    private final Skin skin;
    private final I18NBundle bundle;
    private final BakeryService bakeryService;
    private final Set<Upgrade> upgrades;
    private boolean isExpanded;

    UpgradePanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        super(new Table().left());
        this.skin = skin;
        this.bundle = bundle;
        this.bakeryService = bakeryService;
        this.upgrades = new HashSet<>();
        this.isExpanded = false;
        clip();
        fill();
        top();
        setTransform(true);
        addListener(eventListener());
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
    public void act(float delta) {
        super.act(delta);
        for (var upgrade : Upgrade.values()) {
            addUpgradeButton(upgrade);
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

    private void addUpgradeButton(Upgrade upgrade) {
        if (
            !bakeryService.isUnlocked(upgrade) ||
            upgrades.contains(upgrade)
        ) {
            return;
        }
        var table = getActor();
        table.add(upgradeButton(upgrade));
        if (table.getChildren().size % 5 == 0) {
            table.row();
        }
        upgrades.add(upgrade);
    }

    private UpgradeButton upgradeButton(Upgrade upgrade) {
        var upgradeButton = new UpgradeButton(
            skin,
            bundle,
            bakeryService,
            upgrade
        );
        upgradeButton.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    upgrades.remove(upgrade);
                }
            }
        );
        return upgradeButton;
    }
}
