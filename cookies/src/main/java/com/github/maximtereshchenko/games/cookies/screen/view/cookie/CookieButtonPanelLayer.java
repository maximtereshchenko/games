package com.github.maximtereshchenko.games.cookies.screen.view.cookie;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.CookieService;
import com.github.maximtereshchenko.games.cookies.domain.Event;

import java.util.Random;

final class CookieButtonPanelLayer extends Container<Stack> {

    CookieButtonPanelLayer(
        Skin skin,
        Random random,
        CookieService cookieService,
        EventBus<Event> eventBus
    ) {
        background(skin.get(Style.class).background);
        size(Value.percentWidth(0.4f, this));
        var first = new Flare(skin);
        var second = new Flare(skin);
        second.act(Flare.CYCLE_TIME / 2);
        var stack = new Stack();
        stack.add(first);
        stack.add(second);
        stack.add(new CursorRingsWidget(skin, eventBus));
        stack.add(
            new CookieButton(
                skin,
                random,
                cookieService
            )
        );
        setActor(stack);
    }

    private static final class Style {

        Drawable background;
    }
}
