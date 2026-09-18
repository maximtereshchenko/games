package com.github.maximtereshchenko.games.cookies.screen.view.game.options;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class VolumeWidget extends Table {

    VolumeWidget(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        var gameAssets = assets.game();
        var skin = assetManager.get(gameAssets.skin());
        var bundle = assetManager.get(gameAssets.bundle());
        background(skin.get(Style.class).background);
        var slider = new Slider(
            0,
            1,
            0.05f,
            false,
            skin
        );
        var volumeValueLabel = new VolumeValueLabel(
            assetManager,
            assets
        );
        slider.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    var volume = slider.getValue();
                    volumeValueLabel.setValue(volume);
                    bakeryService.updateVolume(volume);
                }
            }
        );
        slider.setValue(bakeryService.volume());
        add(
            new Label(
                bundle.get("options.slider.volume"),
                skin,
                "volume"
            )
        )
            .left();
        add(volumeValueLabel)
            .right()
            .row();
        add(slider)
            .colspan(2)
            .growX();
    }

    private static final class Style {

        Drawable background;
    }
}
