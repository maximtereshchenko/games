package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;

public final class LoadingView extends Table {

    private final ProgressBar progressBar;

    public LoadingView(I18NBundle bundle, Skin skin) {
        this.progressBar = new ProgressBar(0, 1, 0.01f, false, skin);
        defaults().pad(10);
        add(new Label(bundle.get("screens.loading.text"), skin)).row();
        add(progressBar).growX();
    }

    public void updateProgress(float value) {
        progressBar.setValue(value);
    }
}
