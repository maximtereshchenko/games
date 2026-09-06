package com.github.maximtereshchenko.games.common.configuration;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;

import java.io.StringReader;
import java.nio.file.AccessMode;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ConfigurationReaderTest {

    private final Files files = mock();
    private final FileHandle fileHandle = mock();
    private final ConfigurationReader configurationReader =
        new ConfigurationReader();

    @BeforeEach
    void setUp() {
        Gdx.files = files;
        when(files.classpath(anyString())).thenReturn(fileHandle);
    }

    @Test
    void givenAssetDescriptorWithoutParams_thenAssetDescriptorDeserialized() {
        when(fileHandle.reader())
            .thenReturn(
                new StringReader(
                    """
                    {
                       "assetDescriptor": {
                           "fileName": "file"
                       }
                    }
                    """
                )
            );
        assertThat(
            configurationReader.value(
                "",
                new TypeReference<WithAssetDescriptor<Music>>() {}
            )
        )
            .usingRecursiveComparison()
            .isEqualTo(
                new WithAssetDescriptor<>(
                    new AssetDescriptor<>("file", Music.class)
                )
            );
    }

    @Test
    void givenAssetDescriptorWithParams_thenAssetDescriptorDeserialized() {
        when(fileHandle.reader())
            .thenReturn(
                new StringReader(
                    """
                    {
                       "assetDescriptor": {
                           "fileName": "file",
                           "params": {
                               "atlasName": "atlas"
                           }
                       }
                    }
                    """
                )
            );
        var params = new BitmapFontLoader.BitmapFontParameter();
        params.atlasName = "atlas";
        assertThat(
            configurationReader.value(
                "",
                new TypeReference<WithAssetDescriptor<BitmapFont>>() {}
            )
        )
            .usingRecursiveComparison()
            .isEqualTo(
                new WithAssetDescriptor<>(
                    new AssetDescriptor<>(
                        "file",
                        BitmapFont.class,
                        params
                    )
                )
            );
    }

    @Test
    void givenEnumString_thenEnumDeserialized() {
        when(fileHandle.reader())
            .thenReturn(
                new StringReader(
                    """
                    {
                       "value": "EXECUTE"
                    }
                    """
                )
            );
        assertThat(
            configurationReader.value(
                "",
                new TypeReference<WithEnum<AccessMode>>() {}
            )
        )
            .usingRecursiveComparison()
            .isEqualTo(new WithEnum<>(AccessMode.EXECUTE));
    }

    @Test
    void givenEnumObject_thenEnumDeserialized() {
        when(fileHandle.reader())
            .thenReturn(
                new StringReader(
                    """
                    {
                       "value": {
                           "type": "java.nio.file.AccessMode",
                           "value": "EXECUTE"
                       }
                    }
                    """
                )
            );
        assertThat(
            configurationReader.value(
                "",
                new TypeReference<WithEnum<AccessMode>>() {}
            )
        )
            .usingRecursiveComparison()
            .isEqualTo(new WithEnum<>(AccessMode.EXECUTE));
    }

    @Test
    void givenCollection_thenCollectionDeserialized() {
        when(fileHandle.reader())
            .thenReturn(
                new StringReader(
                    """
                    ["value"]
                    """
                )
            );
        assertThat(
            configurationReader.value(
                "",
                new TypeReference<List<String>>() {}
            )
        )
            .containsExactly("value");
    }

    @Test
    void givenMap_thenMapDeserialized() {
        when(fileHandle.reader())
            .thenReturn(
                new StringReader(
                    """
                    {
                       "a": 1,
                       "b": 2
                    }
                    """
                )
            );
        assertThat(
            configurationReader.value(
                "",
                new TypeReference<Map<String, Integer>>() {}
            )
        )
            .hasSize(2)
            .containsEntry("a", 1)
            .containsEntry("b", 2);
    }

    @Test
    void givenObjectWithType_thenObjectDeserialized() {
        when(fileHandle.reader())
            .thenReturn(
                new StringReader(
                    """
                    {
                       "type": "com.github.maximtereshchenko.games.common.configuration.ConfigurationReaderTest$SimpleRecord",
                       "value": "value"
                    }
                    """
                )
            );
        assertThat(
            configurationReader.value(
                "",
                new TypeReference<Object>() {}
            )
        )
            .isEqualTo(new SimpleRecord("value"));
    }

    record WithAssetDescriptor<T>(AssetDescriptor<T> assetDescriptor) {}

    record WithEnum<T extends Enum<T>>(T value) {}

    record SimpleRecord(String value) {}
}
