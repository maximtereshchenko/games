package com.github.maximtereshchenko.snakes.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public final class CreditsView extends Table {

    private final BasicButton backButton;

    public CreditsView(I18NBundle bundle, Skin skin) {
        backButton = new BasicButton(bundle.get("screens.credits.buttons.back"), skin);
        var label = new Label(bundle.get("screens.credits.text"), skin);
        label.setAlignment(Align.center);
        defaults().growX().pad(3);
        add(label).row();
        add(backButton);
    }

    public void onFinish(Runnable runnable) {
        backButton.onClick(runnable);
    }
}
