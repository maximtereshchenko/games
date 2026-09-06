package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingNameLabel extends Label {

    private final I18NBundle bundle;
    private final BakeryService bakeryService;
    private final Building building;
    private boolean isLocked;

    BuildingNameLabel(
        Skin skin,
        String styleName,
        I18NBundle bundle,
        BakeryService bakeryService,
        Building building
    ) {
        super(
            bundle.get("buildings.names.locked"),
            skin,
            styleName
        );
        this.bundle = bundle;
        this.bakeryService = bakeryService;
        this.building = building;
        this.isLocked = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isLocked && balanceGreaterThanTransactionValue()) {
            setText(bundle.get("buildings.names." + building.name()));
            isLocked = false;
        }
    }

    private boolean balanceGreaterThanTransactionValue() {
        return bakeryService.balance()
                   .compareTo(
                       bakeryService.transactionValue(building)
                   ) >= 0;
    }
}
