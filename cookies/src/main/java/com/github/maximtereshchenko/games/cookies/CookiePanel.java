package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import java.util.Random;

final class CookiePanel extends Container<Stack> {

    CookiePanel(Skin skin, Random random) {
        fill();
        clip();
        var stack = new Stack();
        stack.add(new GradientPanelLayer(skin));
        stack.add(new StaticFallingCookiesWidget(skin));
        stack.add(new DynamicFallingCookiesWidget(skin, random));
        stack.add(new CookieButtonPanelLayer(skin, random));
        stack.add(new CookiesPanelLayer(skin));
        stack.add(new FlowingMilkLayer(skin));
        setActor(stack);
    }
}
