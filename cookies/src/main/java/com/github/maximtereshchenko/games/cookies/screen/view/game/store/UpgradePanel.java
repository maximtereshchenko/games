package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.HashSet;
import java.util.Set;

final class UpgradePanel extends Container<Table> implements EventListener {

    private final Skin skin;
    private final I18NBundle bundle;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;
    private final Set<Upgrade> upgrades;
    private final EventListener delegateListener;
    private boolean isExpanded;

    UpgradePanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(new Table().left());
        this.skin = skin;
        this.bundle = bundle;
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
        this.upgrades = new HashSet<>();
        this.delegateListener = new DelegateListener();
        this.isExpanded = false;
        clip();
        fill();
        top();
        setTransform(true);
        addListener(this);
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

    @Override
    public boolean handle(Event event) {
        return delegateListener.handle(event);
    }

    private void addUpgradeButton(Upgrade upgrade) {
        if (
            !bakeryService.isUnlocked(upgrade) ||
            upgrades.contains(upgrade)
        ) {
            return;
        }
        addActors(upgradeButton(upgrade));
        upgrades.add(upgrade);
    }

    private UpgradeButton upgradeButton(Upgrade upgrade) {
        var upgradeButton = new UpgradeButton(
            skin,
            bundle,
            bigDecimalFormatter,
            bakeryService,
            upgrade
        );
        upgradeButton.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    var table = getActor();
                    var children = table.getChildren().toArray();
                    table.clearChildren();
                    addActors(children);
                    upgrades.remove(upgrade);
                }
            }
        );
        return upgradeButton;
    }

    private void addActors(Actor... actors) {
        var table = getActor();
        for (var actor : actors) {
            table.add(actor);
            if (table.getChildren().size % 5 == 0) {
                table.row();
            }
        }
    }

    private final class DelegateListener extends InputListener {

        @Override
        public void enter(
            InputEvent event,
            float x,
            float y,
            int pointer,
            Actor fromActor
        ) {
            setExpanded(
                pointer,
                fromActor,
                event.getListenerActor(),
                true
            );
        }

        @Override
        public void exit(
            InputEvent event,
            float x,
            float y,
            int pointer,
            Actor toActor
        ) {
            setExpanded(
                pointer,
                toActor,
                event.getListenerActor(),
                false
            );
        }

        private void setExpanded(
            int pointer,
            Actor related,
            Actor listenerActor,
            boolean isExpanded
        ) {
            if (
                pointer == -1 &&
                !isInside(related, listenerActor) &&
                UpgradePanel.this.isExpanded != isExpanded
            ) {
                UpgradePanel.this.isExpanded = isExpanded;
                invalidateHierarchy();
            }
        }

        private boolean isInside(Actor related, Actor listenerActor) {
            return related != null && related.isDescendantOf(listenerActor);
        }
    }
}
