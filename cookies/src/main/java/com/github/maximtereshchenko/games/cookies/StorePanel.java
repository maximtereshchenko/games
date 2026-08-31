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
        var upgradesBeam = new HorizontalBeam(skin, "Upgrades");
        var upgradePanel = new UpgradePanel(skin);
        var buildingsBeam = new HorizontalBeam(skin, "Buildings");
        var amountPanel = new AmountPanel(skin);
        var buildingButton = new GeneratorButton(skin);
        upgradesBeam.addListener(upgradePanel.eventListener());
        upgradePanel.addListener(upgradesBeam.eventListener());
        amountPanel.addListener(buildingsBeam.eventListener());
        buildingButton.addListener(buildingsBeam.eventListener());
        defaults().width(buildingButton.getPrefWidth())
            .height(Value.prefHeight);
        add(label).growX().row();
        add(upgradesBeam).row();
        add(upgradePanel).row();
        add(buildingsBeam).row();
        add(amountPanel).row();
        add(buildingButton);
        top();
    }
}
