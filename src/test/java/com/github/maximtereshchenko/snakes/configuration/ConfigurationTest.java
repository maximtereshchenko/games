package com.github.maximtereshchenko.snakes.configuration;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.snakes.UserProfileStatistics;
import com.github.maximtereshchenko.snakes.session.Colored;
import com.github.maximtereshchenko.snakes.session.Position;
import com.github.maximtereshchenko.snakes.session.SessionStatistics;
import com.github.maximtereshchenko.snakes.session.WorldDimensions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

final class ConfigurationTest {

    @Test
    void givenJson_thenConfigurationDeserialized() throws IOException {
        try (
            var reader = new InputStreamReader(
                Objects.requireNonNull(
                    Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("configuration-test.json")
                )
            )
        ) {
            var parameter = new BitmapFontLoader.BitmapFontParameter();
            parameter.atlasName = "atlas";
            var color = new Color(0x00000000);
            assertThat(Configuration.from(reader))
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
                                List.<Object[]>of(
                                    new Object[]{new Position(6, 7)}
                                ),
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
                                List.<Object[]>of(
                                    new Object[]{Colored.HEAD}
                                ),
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
    }
}
