package com.github.maximtereshchenko.games.bricks.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class ConfigurationReaderTest {

    private final ConfigurationReader configurationReader;

    ConfigurationReaderTest() {
        var configurationDeserializers = new ConfigurationDeserializers();
        configurationDeserializers.addDeserializer(
            CellDefinition.class,
            new CellDefinitionDeserializer()
        );
        configurationDeserializers.addDeserializer(
            AssetDescriptor.class,
            new AssetDescriptorDeserializer()
        );
        this.configurationReader = new ConfigurationReader(configurationDeserializers);
    }

    @BeforeEach
    void setUp() {
        Gdx.files = new Lwjgl3Files();
    }

    @Test
    void givenJson_thenConfigurationDeserialized() {
        var configuration = configurationReader.value(
            "configuration-test.json",
            new TypeReference<Configuration>() {}
        );
        assertThat(configuration)
            .usingRecursiveComparison()
            .isEqualTo(
                new Configuration(
                    "preferences",
                    Map.of("easy", "easy.json"),
                    List.of("level-1.json"),
                    new Configuration.Assets(
                        new AssetDescriptor<>("textures.atlas", TextureAtlas.class),
                        new AssetDescriptor<>("skin.json", Skin.class),
                        new AssetDescriptor<>("loading", I18NBundle.class),
                        new AssetDescriptor<>("game", I18NBundle.class),
                        new AssetDescriptor<>("main.mp3", Music.class),
                        new AssetDescriptor<>("session.mp3", Music.class),
                        new AssetDescriptor<>("ball.wav", Sound.class),
                        new AssetDescriptor<>("bonus.wav", Sound.class),
                        new AssetDescriptor<>("lose.wav", Sound.class),
                        new AssetDescriptor<>("win.wav", Sound.class)
                    ),
                    new Configuration.Dimensions(50, 75),
                    new Configuration.Dimensions(480, 720),
                    "common-blueprints.json",
                    new Configuration.Background(
                        new Configuration.Background.UserInterface(
                            "square",
                            new Color(0.22f, 0, 0.42f, 1)
                        ),
                        new Configuration.Background.Session(
                            "square",
                            new Color(0.1333f, 0, 0.31f, 1)
                        )
                    ),
                    3,
                    "heart",
                    "fullStar",
                    new Configuration.LevelStars("fullStar", "emptyStar"),
                    1.0f,
                    0.5f
                )
            );
        assertThat(configuration.assets().loadingAssets()).hasSize(3);
        assertThat(configuration.assets().gameAssets()).hasSize(7);
    }

    @Test
    void givenJson_thenBlueprintsDeserialized() {
        var blueprints = configurationReader.value(
            "blueprints-test.json",
            new TypeReference<Map<String, List<Object>>>() {}
        );
        assertThat(blueprints.get("paddle"))
            .extracting(component -> component.getClass().getSimpleName())
            .containsExactly("Paddle", "BodyType", "Rectangle");
    }

    @Test
    void givenJson_thenCellsDeserialized() {
        var cells = configurationReader.value(
            "cells-test.json",
            new TypeReference<List<List<CellDefinition>>>() {}
        );
        assertThat(cells)
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    List.of(
                        new BrickDefinition(new Color(1, 0, 0, 1)),
                        new WallDefinition(),
                        new EmptyCellDefinition()
                    )
                )
            );
    }
}
