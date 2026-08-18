package com.github.maximtereshchenko.games.bricks.configuration;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.ConstructorDetector;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class ConfigurationReader {

    private final JsonMapper jsonMapper;

    public ConfigurationReader(
        ConfigurationDeserializers configurationDeserializers
    ) {
        this.jsonMapper = JsonMapper.builder()
            .addModule(
                new SimpleModule()
                    .setDeserializers(configurationDeserializers)
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
            .build();
    }

    public <T> T value(String name, TypeReference<T> typeReference) {
        try (var reader = Gdx.files.classpath(name).reader()) {
            return jsonMapper.readValue(reader, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
