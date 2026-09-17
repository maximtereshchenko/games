package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.I18NBundle;

import com.github.maximtereshchenko.games.cookies.domain.*;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BuffNameLabel extends Label {

    private final I18NBundle bundle;
    private final BakeryService bakeryService;
    private final Buff buff;

    BuffNameLabel(
        AssetManager assetManager,
        Assets assets,
        String styleName,
        BakeryService bakeryService,
        Buff buff
    ) {
        super("", assetManager.get(assets.game().skin()), styleName);
        this.bundle = assetManager.get(assets.game().bundle());
        this.bakeryService = bakeryService;
        this.buff = buff;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bundle.format(
                "buff.%s.name".formatted(buff.name()),
                switch (bakeryService.buffEffect(buff)) {
                    case FrenzyEffect _, ClickFrenzyEffect _ -> new Object[0];
                    case BuildingSpecialEffect buildingSpecialEffect -> new Object[]{
                        buildingSpecialEffect.building().ordinal()
                    };
                }
            )
        );
    }
}
