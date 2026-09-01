package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;

final class GeneratorDetailsPanel extends Table {

    private final Label enabledCookiesLabel;
    private final Label disabledCookiesLabel;
    private final Cell<?> cell;

    GeneratorDetailsPanel(
        Skin skin,
        I18NBundle bundle,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        this.enabledCookiesLabel = new Label(
            "15",
            skin,
            "label_generatorCookies"
        );
        this.disabledCookiesLabel = new Label(
            "15",
            skin,
            "label_generatorCookies_disabled"
        );
        add(new GeneratorNameLabel(skin, bundle, generator, eventBus))
            .colspan(2)
            .left()
            .padBottom(4)
            .row();
        add(new Image(skin.get(Style.class).icon))
            .width(Value.prefWidth)
            .padTop(2);
        this.cell = this.add().expandX().left();
        disable();
    }

    void enable() {
        cell.setActor(enabledCookiesLabel);
    }

    void disable() {
        cell.setActor(disabledCookiesLabel);
    }

    private static final class Style {

        Drawable icon;
    }
}
