package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.util.Random;

final class CookieWidget extends Container<Stack> {

    CookieWidget(
        Skin skin,
        Random random,
        BakeryService bakeryService
    ) {
        background(skin.get(Style.class).background);
        size(Value.percentWidth(0.4f, this));
        var first = new FlareWidget(skin);
        var second = new FlareWidget(skin);
        second.act(FlareWidget.CYCLE_TIME / 2);
        var stack = new Stack();
        stack.add(first);
        stack.add(second);
        stack.add(new CursorRingsWidget(skin, bakeryService));
        stack.add(
            new CookieButton(
                skin,
                random,
                bakeryService
            )
        );
        setActor(stack);
    }

    private static final class Style {

        Drawable background;
    }
}
