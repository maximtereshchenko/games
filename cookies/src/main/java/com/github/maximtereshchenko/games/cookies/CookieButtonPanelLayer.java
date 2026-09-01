package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import java.util.Random;

final class CookieButtonPanelLayer extends Container<Stack> {

    CookieButtonPanelLayer(
        Skin skin,
        Random random,
        CookieService cookieService
    ) {
        background(skin.getDrawable("gradient_borders"));
        size(Value.percentWidth(0.4f, this));
        var first = new Flare(skin);
        var second = new Flare(skin);
        second.act(Flare.CYCLE_TIME / 2);
        var stack = new Stack();
        stack.add(first);
        stack.add(second);
        stack.add(new CursorRingsWidget(skin));
        stack.add(
            new CookieButton(
                skin,
                random,
                cookieService
            )
        );
        setActor(stack);
    }
}
