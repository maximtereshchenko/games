package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.Random;

public final class StorePanel extends Table {

    public StorePanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        background(assetManager.get(assets.game().skin()).get(Style.class).background);
        var upgradesBeam = new StoreBeamWidget(
            assetManager,
            assets,
            bundle.get("store.section.upgrades")
        );
        var upgradePanel = new UpgradePanel(
            assetManager,
            assets,
            bigDecimalFormatter,
            bakeryService,
            random
        );
        var buildingsBeam = new StoreBeamWidget(
            assetManager,
            assets,
            bundle.get("store.section.buildings")
        );
        var transaction = new Transaction(bakeryService);
        var transactionPanel = new TransactionConfigurationPanel(
            assetManager,
            assets,
            bakeryService,
            transaction
        );
        upgradesBeam.addListener(upgradePanel);
        upgradePanel.addListener(upgradesBeam);
        transactionPanel.addListener(buildingsBeam);
        var buildingPanel = new BuildingPanel(
            assetManager,
            assets,
            buildingsBeam,
            bigDecimalFormatter,
            transaction,
            bakeryService,
            random
        );
        defaults()
            .width(new Value() {

                @Override
                public float get(Actor context) {
                    return buildingPanel.getPrefWidth();
                }
            })
            .height(Value.prefHeight);
        add(new StoreLabel(assetManager, assets)).growX().row();
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
