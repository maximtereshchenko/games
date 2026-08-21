package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public final class LevelSelectionView extends Table {

    private final List<Button> buttons;

    public LevelSelectionView(
        Skin skin,
        Configuration configuration
    ) {
        this.buttons = new ArrayList<>();
        defaults()
            .pad(Value.percentHeight(0.01f, this))
            .width(Value.percentWidth(0.6f, this));
        var levelFileNames = configuration.levels();
        for (var i = 0; i < levelFileNames.size(); i++) {
            var button = new TextButton(String.valueOf(i + 1), skin);
            add(button).row();
            buttons.add(button);
        }
    }

    public void onLevelSelected(IntConsumer consumer) {
        for (var i = 0; i < buttons.size(); i++) {
            var level = i;
            buttons.get(i)
                .addListener(
                    new ChangeListener() {

                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            consumer.accept(level);
                        }
                    }
                );
        }
    }
}
