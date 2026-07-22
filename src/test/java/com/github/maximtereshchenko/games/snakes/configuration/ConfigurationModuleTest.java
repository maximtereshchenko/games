package com.github.maximtereshchenko.games.snakes.configuration;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.snakes.UserProfileStatistics;
import com.github.maximtereshchenko.games.snakes.session.*;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class ConfigurationModuleTest {

    private final JsonMapper jsonMapper = JsonMapper.builder()
        .addModule(new ConfigurationModule())
        .build();

    @Test
    void whenReadValue_thenConfigurationDeserialized() throws IOException {
        try (
            var stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("configuration-test.json")
        ) {
            var parameter = new BitmapFontLoader.BitmapFontParameter();
            parameter.atlasName = "atlas";
            var color = new Color(0x00000000);
            assertThat(jsonMapper.readValue(stream, Configuration.class))
                .usingRecursiveComparison()
                .isEqualTo(
                    new Configuration(
                        "preferences",
                        new WorldDimensions(1, 2),
                        3,
                        4,
                        new Position(5, 6),
                        Direction.RIGHT,
                        7,
                        new Assets(
                            new AssetDescriptor<>("skin", Skin.class),
                            new AssetDescriptor<>("loading", I18NBundle.class),
                            new AssetDescriptor<>("font", BitmapFont.class, parameter),
                            new AssetDescriptor<>("game", I18NBundle.class),
                            new AssetDescriptor<>("music", Music.class)
                        ),
                        0.8f,
                        List.of(
                            new Mode(
                                "first",
                                0.9f,
                                10,
                                11,
                                Edge.OPPOSITE,
                                Set.of(RelativeDirection.LEFT),
                                Map.of(
                                    Colored.BACKGROUND, color,
                                    Colored.HEAD, color,
                                    Colored.SEGMENT, color,
                                    Colored.FOOD, color,
                                    Colored.FOOD_EATEN_COUNTER, color,
                                    Colored.WARP, color
                                ),
                                new ModeUnlockRequirements(
                                    Map.of(UserProfileStatistics.LAUNCHES, 12),
                                    Map.of()
                                )
                            ),
                            new Mode(
                                "second",
                                1.3f,
                                14,
                                15,
                                Edge.LEFT,
                                Set.of(RelativeDirection.RIGHT),
                                Map.of(
                                    Colored.BACKGROUND, color,
                                    Colored.HEAD, color,
                                    Colored.SEGMENT, color,
                                    Colored.FOOD, color,
                                    Colored.FOOD_EATEN_COUNTER, color,
                                    Colored.WARP, color
                                ),
                                new ModeUnlockRequirements(
                                    Map.of(),
                                    Map.of(SessionStatistics.LEFT_TURNS, 16)
                                )
                            )
                        )
                    )
                );
        }
    }
}
