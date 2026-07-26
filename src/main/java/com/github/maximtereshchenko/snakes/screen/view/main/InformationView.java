package com.github.maximtereshchenko.snakes.screen.view.main;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public final class InformationView extends Table {

    private final Label titleLabel;
    private final Label descriptionLabel;

    public InformationView(Skin skin) {
        titleLabel = new Label("", skin);
        descriptionLabel = new Label("", skin);
        descriptionLabel.setWrap(true);
        defaults().growX();
        add(titleLabel).row();
        add(descriptionLabel);
    }

    public void update(String title, String description) {
        titleLabel.setText(title);
        descriptionLabel.setText(description);
    }
}
