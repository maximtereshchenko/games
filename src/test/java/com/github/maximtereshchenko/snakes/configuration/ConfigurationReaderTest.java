package com.github.maximtereshchenko.snakes.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.snakes.UserProfileStatistics;
import com.github.maximtereshchenko.snakes.session.Colored;
import com.github.maximtereshchenko.snakes.session.Position;
import com.github.maximtereshchenko.snakes.session.SessionStatistics;
import com.github.maximtereshchenko.snakes.session.WorldDimensions;
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
                                Colored.BACKGROUND, color,
                                Colored.HEAD, color,
                                Colored.SEGMENT, color,
                                Colored.FOOD, color,
                                Colored.INTERFACE, color,
                                Colored.WARP, color
                            ),
                            new ModeUnlockRequirements(
                                Map.of(UserProfileStatistics.LAUNCHES, 8),
                                Map.of()
                            )
                        ),
                        new Mode(
                            "second",
                            new WorldDimensions(9, 10),
                            1.1f,
                            "second.json",
                            Map.of(
                                Colored.BACKGROUND, color,
                                Colored.HEAD, color,
                                Colored.SEGMENT, color,
                                Colored.FOOD, color,
                                Colored.INTERFACE, color,
                                Colored.WARP, color
                            ),
                            new ModeUnlockRequirements(
                                Map.of(),
                                Map.of(SessionStatistics.LEFT_TURNS, 13)
                            )
                        )
                    )
                )
            );
    }

    @Test
    void givenJson_thenEntitiesDeserialized() {
        when(mode.entities()).thenReturn("entities-test.json");
        assertThat(configurationReader.entities(mode))
            .isDeepEqualTo(
                new Object[][]{
                    new Object[]{new Position(6, 7)},
                    new Object[]{Colored.HEAD},
                }
            );
    }
}
