package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.Random;

final class CookieWidget extends Container<Stack> {

    private final Style style;
    private final BakeryService bakeryService;

    CookieWidget(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        this.style = assetManager.get(assets.game().skin()).get(Style.class);
        this.bakeryService = bakeryService;
        size(Value.percentWidth(0.4f, this));
        var flareWidget = new FlareWidget(assetManager, assets, bakeryService);
        flareWidget.act(FlareWidget.CYCLE_TIME_SECONDS);
        var stack = new Stack();
        stack.add(new FlareWidget(assetManager, assets, bakeryService));
        stack.add(flareWidget);
        stack.add(new CursorRingsWidget(assetManager, assets, bakeryService));
        stack.add(
            new CookieButton(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService,
                random
            )
        );
        setActor(stack);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setBackground(background());
    }

    private Drawable background() {
        if (hasActiveBuff()) {
            return style.activeBuffBackground;
        }
        return style.standardBackground;
    }

    private boolean hasActiveBuff() {
        for (var buff : Buff.values()) {
            if (bakeryService.buffInterval(buff).progress() < 1) {
                return true;
            }
        }
        return false;
    }

    private static final class Style {

        Drawable standardBackground;
        Drawable activeBuffBackground;
    }
}
