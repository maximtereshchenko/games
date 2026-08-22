package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;

public final class LoadingView extends Root {

    private final ProgressBar progressBar;

    public LoadingView(
        I18NBundle bundle,
        Skin skin,
        Configuration configuration,
        AssetManager assetManager
    ) {
        super(configuration, assetManager);
        this.progressBar = new ProgressBar(0, 1, 0.01f, false, skin);
        defaults()
            .pad(Value.percentHeight(0.01f, this))
            .width(Value.percentWidth(0.9f, this));
        var label = new Label(bundle.get("screens.loading.text"), skin);
        label.setAlignment(Align.center);
        add(label).row();
        add(progressBar).growX();
    }

    public void updateProgress(float value) {
        progressBar.setValue(value);
    }
}
