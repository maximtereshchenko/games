package com.github.maximtereshchenko.games.cookies.screen.view.display;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.Building;

import java.util.Random;

final class BuildingImage extends Image {

    private final int row;

    BuildingImage(
        Skin skin,
        Building building,
        Random random,
        int index
    ) {
        var style = skin.get(building.name(), Style.class);
        super(style.drawable);
        this.row = index % style.rows;
        var column = index / style.rows;
        setPosition(
            (column + (float) row / style.rows + jitter(random) + 0.5f) * getWidth(),
            ((style.rows - 1f - row) / style.rows + jitter(random) + 0.15f) * getHeight()
        );
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        setZIndex(row);
    }

    private float jitter(Random random) {
        return random.nextFloat(-0.1f, 0.1f);
    }

    private static final class Style {

        Drawable drawable;
        int rows;
    }
}
