package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import java.util.Random;

final class CookieButtonStack extends Stack {

    CookieButtonStack(Skin skin, Random random) {
        var first = new Flare(skin);
        var second = new Flare(skin);
        second.act(Flare.CYCLE_TIME / 2);
        add(first);
        add(second);
        add(new CursorRingWidget(skin));
        add(new CookieButton(skin, random));
    }
}
