package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;

final class StorePanel extends Table {

    StorePanel(Skin skin) {
        background(skin.getDrawable("tile_black_transparent50"));
        var label = new Label("Store", skin, "label_store");
        label.setAlignment(Align.center);
        var upgradesSeparator = new StoreSeparator(skin, "Upgrades");
        var upgradePanel = new UpgradePanel(skin);
        var buildingsSeparator = new StoreSeparator(skin, "Buildings");
        var amountPanel = new AmountPanel(skin);
        var buildingButton = new BuildingButton(skin);
        upgradesSeparator.addListener(upgradePanel.eventListener());
        upgradePanel.addListener(upgradesSeparator.eventListener());
        amountPanel.addListener(buildingsSeparator.eventListener());
        buildingButton.addListener(buildingsSeparator.eventListener());
        defaults().width(buildingButton.getPrefWidth())
            .height(Value.prefHeight);
        add(label).growX().row();
        add(upgradesSeparator).row();
        add(upgradePanel).row();
        add(buildingsSeparator).row();
        add(amountPanel).height(Value.minHeight).row();
        add(buildingButton);
        top();
    }
}
