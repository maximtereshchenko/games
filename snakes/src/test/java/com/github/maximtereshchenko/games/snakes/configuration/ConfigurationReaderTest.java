package com.github.maximtereshchenko.games.snakes.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.snakes.UserProfileMetric;
import com.github.maximtereshchenko.games.snakes.session.PaletteColor;
import com.github.maximtereshchenko.games.snakes.session.SessionMetric;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ConfigurationReaderTest {

    private final Mode mode = mock();
    private final ConfigurationReader configurationReader =
        new ConfigurationReader();

    @BeforeEach
    void setUp() {
        Gdx.files = new Lwjgl3Files();
    }

    @Test
    void givenJson_thenConfigurationDeserialized() {
        var parameter = new BitmapFontLoader.BitmapFontParameter();
        parameter.atlasName = "atlas";
        var color = new Color(0x00000000);
        assertThat(configurationReader.configuration("configuration-test.json"))
            .usingRecursiveComparison()
            .isEqualTo(
                new Configuration(
                    "preferences",
                    new Assets(
                        new AssetDescriptor<>("skin", Skin.class),
                        new AssetDescriptor<>("loading", I18NBundle.class),
                        new AssetDescriptor<>("font", BitmapFont.class, parameter),
                        new AssetDescriptor<>("game", I18NBundle.class),
                        new AssetDescriptor<>("music", Music.class)
                    ),
                    0.1f,
                    List.of(
                        new Mode(
                            "first",
                            new WorldDimensions(2, 3),
                            0.4f,
                            "first.json",
                            Map.of(
                                PaletteColor.BACKGROUND, color,
                                PaletteColor.HEAD, color,
                                PaletteColor.SEGMENT, color,
                                PaletteColor.FOOD, color,
                                PaletteColor.INTERFACE, color,
                                PaletteColor.WARP, color
                            ),
                            new ModeUnlockRequirements(
                                Map.of(UserProfileMetric.LAUNCHES, 8),
                                Map.of()
                            )
                        ),
                        new Mode(
                            "second",
                            new WorldDimensions(9, 10),
                            1.1f,
                            "second.json",
                            Map.of(
                                PaletteColor.BACKGROUND, color,
                                PaletteColor.HEAD, color,
                                PaletteColor.SEGMENT, color,
                                PaletteColor.FOOD, color,
                                PaletteColor.INTERFACE, color,
                                PaletteColor.WARP, color
                            ),
                            new ModeUnlockRequirements(
                                Map.of(),
                                Map.of(SessionMetric.LEFT_TURNS, 13)
                            )
                        )
                    )
                )
            );
    }

    @Test
    void givenJson_thenEntitiesDeserialized() {
        when(mode.entities()).thenReturn("entities-test.json");
        var entities = configurationReader.entities(mode);
        assertThat(entities).hasNumberOfRows(2);
        assertThat(entities[0])
            .satisfiesExactly(
                worldPosition -> assertThat(worldPosition)
                    .extracting("x", "y")
                    .containsExactly(6, 7)
            );
        assertThat(entities[1]).containsExactly(PaletteColor.HEAD);
    }
}
