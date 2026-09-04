package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Event;

public final class StorePanel extends Table {

    public StorePanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        EventBus<Event> eventBus
    ) {
        background(skin.get(Style.class).background);
        var upgradesBeam = new StoreBeam(
            skin,
            bundle.get("views.bakery.panels.store.labels.upgrades")
        );
        var upgradePanel = new UpgradePanel(skin, eventBus);
        var buildingsBeam = new StoreBeam(
            skin,
            bundle.get("views.bakery.panels.store.labels.buildings")
        );
        var transactionModePanel = new TransactionModePanel(skin);
        upgradesBeam.addListener(upgradePanel.eventListener());
        upgradePanel.addListener(upgradesBeam.eventListener());
        transactionModePanel.addListener(buildingsBeam.eventListener());
        var buildingPanel = new BuildingPanel(
            skin,
            bundle,
            buildingsBeam.eventListener(),
            bakeryService,
            eventBus
        );
        defaults()
            .width(buildingPanel.getPrefWidth())
            .height(Value.prefHeight);
        add(new StoreLabel(skin, bundle)).growX().row();
        add(upgradesBeam).row();
        add(upgradePanel).row();
        add(buildingsBeam).row();
        add(transactionModePanel).row();
        add(buildingPanel);
        top();
    }

    private static final class Style {

        Drawable background;
    }
}
