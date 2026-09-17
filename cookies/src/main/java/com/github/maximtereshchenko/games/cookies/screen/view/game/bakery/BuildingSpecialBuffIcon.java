package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.domain.BuildingSpecialEffect;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.HashMap;

final class BuildingSpecialBuffIcon extends Image {

    private final Style style;
    private final BakeryService bakeryService;

    BuildingSpecialBuffIcon(AssetManager assetManager, Assets assets, BakeryService bakeryService) {
        this.style = assetManager.get(assets.game().skin()).get(Style.class);
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
