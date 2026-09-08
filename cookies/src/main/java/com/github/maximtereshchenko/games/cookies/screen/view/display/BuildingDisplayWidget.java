package com.github.maximtereshchenko.games.cookies.screen.view.display;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

import java.util.Random;

final class BuildingDisplayWidget extends Container<WidgetGroup> {

    private final Skin skin;
    private final BakeryService bakeryService;
    private final Building building;
    private final Random random;

    BuildingDisplayWidget(
        Skin skin,
        BakeryService bakeryService,
        Building building,
        Random random
    ) {
        super(new WidgetGroup());
        this.skin = skin;
        this.bakeryService = bakeryService;
        this.building = building;
        this.random = random;
        getActor().setTransform(true);
        fill();
        clip();
        background(skin.get(building.name(), Style.class).background);
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
                    skin,
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
