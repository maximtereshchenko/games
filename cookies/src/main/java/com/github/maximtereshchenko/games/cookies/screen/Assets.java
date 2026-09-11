package com.github.maximtereshchenko.games.cookies.screen;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Configuration;

import java.util.Set;

public record Assets(
    Loading loading,
    Game game
) {

    public record Loading(
        AssetDescriptor<Skin> skin,
        AssetDescriptor<I18NBundle> bundle
    ) {

        Set<AssetDescriptor<?>> all() {
            return Set.of(skin, bundle);
        }
    }

    public record Game(
        AssetDescriptor<Configuration> configuration,
        AssetDescriptor<Skin> skin,
        AssetDescriptor<I18NBundle> bundle
    ) {

        Set<AssetDescriptor<?>> all() {
            return Set.of(configuration, skin, bundle);
        }
    }
}
