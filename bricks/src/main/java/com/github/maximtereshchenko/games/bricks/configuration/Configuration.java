package com.github.maximtereshchenko.games.bricks.configuration;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record Configuration(
    String preferencesName,
    Map<String, String> difficulties,
    List<String> levels,
    Assets assets,
    Dimensions worldDimensions,
    Dimensions interfaceDimensions,
    String commonBlueprints,
    Background background,
    int maxStars,
    String livesIndicatorTexture,
    String starsIndicatorTexture,
    LevelStars levelStars
) {

    public record Assets(
        AssetDescriptor<TextureAtlas> textureAtlas,
        AssetDescriptor<Skin> skin,
        AssetDescriptor<I18NBundle> loadingBundle,
        AssetDescriptor<I18NBundle> gameBundle,
        AssetDescriptor<Music> mainMusic,
        AssetDescriptor<Music> sessionMusic,
        AssetDescriptor<Sound> ballSound,
        AssetDescriptor<Sound> bonusSound,
        AssetDescriptor<Sound> loseSound,
        AssetDescriptor<Sound> winSound
    ) {

        public Set<AssetDescriptor<?>> loadingAssets() {
            return Set.of(skin, loadingBundle);
        }

        public Set<AssetDescriptor<?>> gameAssets() {
            return Set.of(
                textureAtlas,
                gameBundle,
                mainMusic,
                sessionMusic,
                ballSound,
                bonusSound,
                loseSound,
                winSound
            );
        }
    }

    public record Dimensions(float width, float height) {}

    public record Background(Color color, String texture) {}

    public record LevelStars(String collectedTexture, String missingTexture) {}
}
