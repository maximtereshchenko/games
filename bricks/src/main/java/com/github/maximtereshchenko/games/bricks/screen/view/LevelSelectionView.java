package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public final class LevelSelectionView extends Table {

    private final List<LevelButton> levelButtons;

    public LevelSelectionView(
        Skin skin,
        Configuration configuration,
        UserProfile userProfile,
        AssetManager assetManager,
        String difficulty
    ) {
        this.levelButtons = new ArrayList<>();
        defaults()
            .pad(Value.percentHeight(0.01f, this))
            .width(Value.percentWidth(0.6f, this));
        var levelFileNames = configuration.levels();
        for (var i = 0; i < levelFileNames.size(); i++) {
            var levelButton = new LevelButton(
                skin,
                configuration,
                userProfile,
                assetManager,
                difficulty,
                i
            );
            add(levelButton).row();
            levelButtons.add(levelButton);
        }
    }

    public void onLevelSelected(IntConsumer consumer) {
        levelButtons.forEach(
            levelButton -> levelButton.onLevelSelected(consumer)
        );
    }
}
