package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;

final class GeneratorDetailsPanel extends Table {

    GeneratorDetailsPanel(
        Skin skin,
        I18NBundle bundle,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        add(new GeneratorNameLabel(skin, bundle, generator, eventBus))
            .colspan(2)
            .left()
            .padBottom(4)
            .row();
        add(new SmallCookieIcon(skin, generator, eventBus))
            .width(Value.prefWidth)
            .padTop(2);
        add(new GeneratorCookiesLabel(skin, generator, eventBus))
            .expandX()
            .left();
    }
}
