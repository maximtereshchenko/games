package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

public final class StorePanel extends Table {

    public StorePanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        background(skin.get(Style.class).background);
        var upgradesBeam = new StoreBeamWidget(
            skin,
            bundle.get("upgrades")
        );
        var upgradePanel = new UpgradePanel(
            skin,
            bundle,
            bakeryService
        );
        var buildingsBeam = new StoreBeamWidget(
            skin,
            bundle.get("buildings")
        );
        var transactionModePanel = new TransactionModePanel(skin);
        upgradesBeam.addListener(upgradePanel.eventListener());
        upgradePanel.addListener(upgradesBeam.eventListener());
        transactionModePanel.addListener(buildingsBeam.eventListener());
        var buildingPanel = new BuildingPanel(
            skin,
            bundle,
            buildingsBeam.eventListener(),
            bakeryService
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
