package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public final class MainView extends Table {

    private final Button playButton;
    private final Button settingsButton;

    public MainView(
        I18NBundle bundle,
        Skin skin
    ) {
        this.playButton = new TextButton(
            bundle.get("screens.main.buttons.play"),
            skin
        );
        this.settingsButton = new TextButton(
            bundle.get("screens.main.buttons.settings"),
            skin
        );
        defaults().pad(10).width(200);
        var label = new Label(bundle.get("screens.main.title"), skin);
        label.setAlignment(Align.center);
        add(label).row();
        add(playButton).row();
        add(settingsButton).row();
    }

    public void onPlay(Runnable runnable) {
        playButton.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    runnable.run();
                }
            }
        );
    }
}
