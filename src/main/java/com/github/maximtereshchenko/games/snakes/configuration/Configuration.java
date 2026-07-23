package com.github.maximtereshchenko.games.snakes.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.ConstructorDetector;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;

import java.io.Reader;
import java.util.List;

public record Configuration(
    String preferencesName,
    Assets assets,
    float defaultMusicVolume,
    List<Mode> modes
) {

    public static Configuration from(Reader reader) {
        return JsonMapper.builder()
            .addModule(
                new SimpleModule()
                    .setDeserializers(new ConfigurationDeserializers())
            )
            .activateDefaultTypingAsProperty(
                BasicPolymorphicTypeValidator.builder()
                    .allowIfBaseType(Object.class)
                    .build(),
                DefaultTyping.JAVA_LANG_OBJECT,
                "type"
            )
            .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED)
            .changeDefaultVisibility(
                visibilityChecker -> visibilityChecker.withCreatorVisibility(
                    JsonAutoDetect.Visibility.ANY
                )
            )
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build()
            .readValue(reader, Configuration.class);
    }
}
