package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.domain.BuildingSpecialEffect;

import java.util.HashMap;

final class BuildingSpecialBuffIcon extends Image {

    private final Style style;
    private final BakeryService bakeryService;

    BuildingSpecialBuffIcon(Skin skin, BakeryService bakeryService) {
        this.style = skin.get(Style.class);
        this.bakeryService = bakeryService;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (bakeryService.buffEffect(Buff.BUILDING_SPECIAL) instanceof BuildingSpecialEffect buildingSpecialEffect) {
            setDrawable(style.drawables.get(buildingSpecialEffect.building().name()));
        }
    }

    private static final class Style {

        HashMap<String, Drawable> drawables;
    }
}
