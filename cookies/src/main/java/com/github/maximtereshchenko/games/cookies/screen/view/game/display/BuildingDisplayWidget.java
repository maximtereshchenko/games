package com.github.maximtereshchenko.games.cookies.screen.view.game.display;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.Random;

final class BuildingDisplayWidget extends Container<WidgetGroup> {

    private final AssetManager assetManager;
    private final Assets assets;
    private final BakeryService bakeryService;
    private final Building building;
    private final Random random;

    BuildingDisplayWidget(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Building building,
        Random random
    ) {
        this.assetManager = assetManager;
        this.assets = assets;
        super(new WidgetGroup());
        this.bakeryService = bakeryService;
        this.building = building;
        this.random = random;
        getActor().setTransform(true);
        fill();
        clip();
        background(assetManager.get(assets.game().skin()).get(building.name(), Style.class).background);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        var widgetGroup = getActor();
        for (
            var i = widgetGroup.getChildren().size;
            i < bakeryService.count(building);
            i++
        ) {
            widgetGroup.addActor(
                new BuildingImage(
                    assetManager,
                    assets,
                    building,
                    random,
                    i
                )
            );
        }
    }

    private static final class Style {

        Drawable background;
    }
}
