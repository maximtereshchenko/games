package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ColoredButton extends Container<Button> {

    public ColoredButton(Skin skin, Actor actor) {
        var style = skin.get(Style.class);
        super(new Button(style));
        background(style.background);
        getActor().add(actor);
    }

    public void setDisabled(boolean isDisabled) {
        getActor().setDisabled(isDisabled);
    }

    private static final class Style extends Button.ButtonStyle {

        Drawable background;
    }
}
