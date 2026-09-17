package com.github.maximtereshchenko.games.cookies.screen;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Configuration;

import java.util.HashSet;
import java.util.List;
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
        AssetDescriptor<Preferences> preferences,
        AssetDescriptor<Skin> skin,
        AssetDescriptor<I18NBundle> bundle,
        List<AssetDescriptor<Sound>> cookieSounds,
        List<AssetDescriptor<Sound>> buildingSounds,
        AssetDescriptor<Sound> tickSound,
        AssetDescriptor<Sound> menuOnSound,
        AssetDescriptor<Sound> menuOffSound,
        AssetDescriptor<Sound> goldenCookieSpawnSound,
        AssetDescriptor<Sound> goldenCookieConsumeSound
    ) {

        Set<AssetDescriptor<?>> all() {
            var all = new HashSet<AssetDescriptor<?>>();
            all.add(configuration);
            all.add(preferences);
            all.add(skin);
            all.add(bundle);
            all.addAll(cookieSounds);
            all.addAll(buildingSounds);
            all.add(tickSound);
            all.add(menuOnSound);
            all.add(menuOffSound);
            all.add(goldenCookieSpawnSound);
            all.add(goldenCookieConsumeSound);
            return all;
        }
    }
}
