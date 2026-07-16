package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.snakes.session.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Configuration {

    private final Properties properties;

    Configuration(Properties properties) {
        this.properties = properties;
    }

    public int snakeFoodGrowth() {
        return Integer.parseInt(properties.getProperty("snake.food.growth"));
    }

    public Position snakeHeadPosition() {
        return new Position(
            Integer.parseInt(properties.getProperty("snake.head.x")),
            Integer.parseInt(properties.getProperty("snake.head.y"))
        );
    }

    public Direction snakeHeadDirection() {
        return Direction.valueOf(properties.getProperty("snake.head.direction"));
    }

    public int snakeLength() {
        return Integer.parseInt(properties.getProperty("snake.length"));
    }

    public float interfaceViewportHeight() {
        return Float.parseFloat(properties.getProperty("interface.viewport.height"));
    }

    Preferences preferences() {
        return Gdx.app.getPreferences(properties.getProperty("preferences.name"));
    }

    WorldDimensions worldDimensions() {
        return new WorldDimensions(
            Integer.parseInt(properties.getProperty("world.width")),
            Integer.parseInt(properties.getProperty("world.height"))
        );
    }

    Assets assets() {
        var bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = properties.getProperty("assets.font.atlas.name");
        return new Assets(
            new AssetDescriptor<>(properties.getProperty("assets.skin"), Skin.class),
            new AssetDescriptor<>(properties.getProperty("assets.bundle.loading"), I18NBundle.class),
            new AssetDescriptor<>(properties.getProperty("assets.font.name"), BitmapFont.class, bitmapFontParameter),
            new AssetDescriptor<>(properties.getProperty("assets.bundle.game"), I18NBundle.class)
        );
    }

    List<Mode> modes(UserProfile userProfile) {
        return list("modes")
            .stream()
            .map(name -> mode(userProfile, name))
            .toList();
    }

    private Mode mode(UserProfile userProfile, String name) {
        return new Mode(
            name,
            Float.parseFloat(properties.getProperty("modes.%s.game.interval".formatted(name))),
            list("modes.%s.snake.turns.legal".formatted(name))
                .stream()
                .map(LegalTurn::valueOf)
                .collect(Collectors.toSet()),
            palette(name),
            new ModeUnlockRequirements(
                userProfile,
                userProfileThresholds(name),
                sessionThresholds(name)
            )
        );
    }

    private Map<SessionStatistics, Integer> sessionThresholds(String name) {
        return thresholds(
            SessionStatistics.class,
            SessionStatistics.values(),
            name,
            "session"
        );
    }

    private Map<UserProfileStatistics, Integer> userProfileThresholds(String name) {
        return thresholds(
            UserProfileStatistics.class,
            UserProfileStatistics.values(),
            name,
            "profile"
        );
    }

    private <T extends Enum<T>> Map<T, Integer> thresholds(
        Class<T> enumType,
        T[] enums,
        String mode,
        String propertySegment
    ) {
        return map(
            enumType,
            enums,
            instance -> Integer.parseInt(
                properties.getProperty(
                    "modes.%s.unlock.requirements.%s.%s"
                        .formatted(
                            mode,
                            propertySegment,
                            instance
                        ),
                    "0"
                )
            )
        );
    }

    private Map<Colored, Color> palette(String mode) {
        return map(
            Colored.class,
            Colored.values(),
            colored -> new Color(
                Integer.parseUnsignedInt(
                    properties.getProperty("modes.%s.palette.%s".formatted(mode, colored)),
                    16
                )
            )
        );
    }

    private <T extends Enum<T>, R> Map<T, R> map(
        Class<T> enumType,
        T[] enums,
        Function<T, R> function
    ) {
        var map = new EnumMap<T, R>(enumType);
        for (var enumInstance : enums) {
            map.put(enumInstance, function.apply(enumInstance));
        }
        return map;
    }

    private List<String> list(String property) {
        return List.of(properties.getProperty(property).split(","));
    }
}
