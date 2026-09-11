package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

public final class StorePanel extends Table {

    public StorePanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        background(skin.get(Style.class).background);
        var upgradesBeam = new StoreBeamWidget(
            skin,
            bundle.get("store.section.upgrades")
        );
        var upgradePanel = new UpgradePanel(
            skin,
            bundle,
            bigDecimalFormatter,
            bakeryService
        );
        var buildingsBeam = new StoreBeamWidget(
            skin,
            bundle.get("store.section.buildings")
        );
        var transaction = new Transaction(bakeryService);
        var transactionPanel = new TransactionConfigurationPanel(
            skin,
            bundle,
            transaction
        );
        upgradesBeam.addListener(upgradePanel.eventListener());
        upgradePanel.addListener(upgradesBeam.eventListener());
        transactionPanel.addListener(buildingsBeam.eventListener());
        var buildingPanel = new BuildingPanel(
            skin,
            bundle,
            buildingsBeam.eventListener(),
            bigDecimalFormatter,
            transaction,
            bakeryService
        );
        defaults()
            .width(new Value() {

                @Override
                public float get(Actor context) {
                    return buildingPanel.getPrefWidth();
                }
            })
            .height(Value.prefHeight);
        add(new StoreLabel(skin, bundle)).growX().row();
        add(upgradesBeam).row();
        add(upgradePanel).row();
        add(buildingsBeam).row();
        add(transactionPanel).row();
        add(buildingPanel);
        top();
    }

    private static final class Style {

        Drawable background;
    }
}
