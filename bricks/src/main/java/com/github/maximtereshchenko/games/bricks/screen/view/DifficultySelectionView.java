package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class DifficultySelectionView extends Table {

    private final Map<Button, String> buttons;

    public DifficultySelectionView(
        I18NBundle bundle,
        Skin skin,
        Configuration configuration
    ) {
        this.buttons = new HashMap<>();
        defaults().pad(10).width(200);
        for (var difficulty : configuration.difficulties().keySet()) {
            var button = new TextButton(
                bundle.get(
                    "screens.difficulties.buttons.%s.name"
                        .formatted(difficulty)
                ),
                skin
            );
            add(button).row();
            buttons.put(button, difficulty);
        }
    }

    public void onDifficultySelected(Consumer<String> consumer) {
        for (var entry : buttons.entrySet()) {
            entry.getKey()
                .addListener(
                    new ChangeListener() {

                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            consumer.accept(entry.getValue());
                        }
                    }
                );
        }
    }
}
