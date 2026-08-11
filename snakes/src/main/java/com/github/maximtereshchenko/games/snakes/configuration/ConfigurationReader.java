package com.github.maximtereshchenko.games.snakes.configuration;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.ConstructorDetector;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class ConfigurationReader {

    private final JsonMapper jsonMapper = JsonMapper.builder()
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
        .build();

    public Configuration configuration(String name) {
        return value(name, Configuration.class);
    }

    public Object[][] entities(Mode mode) {
        return value(mode.entities(), Object[][].class);
    }

    private <T> T value(String name, Class<T> type) {
        try (var reader = Gdx.files.classpath(name).reader()) {
            return jsonMapper.readValue(reader, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
