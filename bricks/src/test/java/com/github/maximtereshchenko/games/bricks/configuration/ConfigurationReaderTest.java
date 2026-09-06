package com.github.maximtereshchenko.games.bricks.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.graphics.Color;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class ConfigurationReaderTest {

    private ConfigurationReader configurationReader;

    @BeforeEach
    void setUp() {
        configurationReader = new ConfigurationReader();
        Gdx.files = new Lwjgl3Files();
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
