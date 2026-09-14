package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.Random;

final class CookieWidget extends Container<Stack> {

    CookieWidget(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        background(skin.get(Style.class).background);
        size(Value.percentWidth(0.4f, this));
        var flareWidget = new FlareWidget(skin);
        flareWidget.act(FlareWidget.CYCLE_TIME_SECONDS);
        var stack = new Stack();
        stack.add(new FlareWidget(skin));
        stack.add(flareWidget);
        stack.add(new CursorRingsWidget(skin, bakeryService));
        stack.add(
            new CookieButton(
                skin,
                bigDecimalFormatter,
                bakeryService,
                random
            )
        );
        setActor(stack);
    }

    private static final class Style {

        Drawable background;
    }
}
