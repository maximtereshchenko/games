package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;

import java.util.function.IntConsumer;

final class LevelButton extends TextButton {

    private final int level;

    LevelButton(
        Skin skin,
        Configuration configuration,
        UserProfile userProfile,
        AssetManager assetManager,
        String difficulty,
        int level
    ) {
        super(String.valueOf(level + 1), skin);
        this.level = level;
        setDisabled(
            !userProfile.isUnlocked(
                difficulty,
                level
            )
        );
        row();
        addStars(
            configuration,
            userProfile,
            assetManager,
            difficulty,
            level
        );
    }

    public void onLevelSelected(IntConsumer consumer) {
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    consumer.accept(level);
                }
            }
        );
    }

    private void addStars(
        Configuration configuration,
        UserProfile userProfile,
        AssetManager assetManager,
        String difficulty,
        int level
    ) {
        var levelStars = configuration.levelStars();
        var textureAtlas = assetManager.get(
            configuration.assets()
                .textureAtlas()
        );
        var collected = textureAtlas.findRegion(levelStars.collectedTexture());
        var missing = textureAtlas.findRegion(levelStars.missingTexture());
        var table = new Table();
        table.defaults().pad(Value.percentWidth(0.01f, table));
        for (var i = 0; i < configuration.maxStars(); i++) {
            table.add(
                new Image(
                    textureRegion(
                        i + 1,
                        userProfile.stars(difficulty, level),
                        collected,
                        missing
                    )
                )
            );
        }
        add(table).growX();
    }

    private TextureRegion textureRegion(
        int current,
        int collectedStars,
        TextureRegion collected,
        TextureRegion missing
    ) {
        if (current <= collectedStars) {
            return collected;
        }
        return missing;
    }
}
