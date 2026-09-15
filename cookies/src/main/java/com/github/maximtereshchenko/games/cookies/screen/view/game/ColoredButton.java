package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ColoredButton extends Container<Button> {

    private final Style style;

    public ColoredButton(Skin skin, Actor actor) {
        var buttonStyle = skin.get(Style.class);
        super(new Button(buttonStyle));
        this.style = buttonStyle;
        background(style.background);
        getActor().add(actor);
    }

    public void setDisabled(boolean isDisabled) {
        var button = getActor();
        button.setDisabled(isDisabled);
        button.setColor(getColor());
    }

    @Override
    public Color getColor() {
        if (getActor().isDisabled()) {
            return style.disabledColor;
        }
        return style.enabledColor;
    }

    private static final class Style extends Button.ButtonStyle {

        Drawable background;
        Color enabledColor;
        Color disabledColor;
    }
}
