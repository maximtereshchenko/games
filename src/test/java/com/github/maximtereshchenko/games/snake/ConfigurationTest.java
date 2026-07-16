package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ConfigurationTest {

    private final Properties properties = new Properties();
    private final UserProfile userProfile = mock();
    private final Preferences preferences = mock();
    private final Configuration configuration = new Configuration(properties);

    @Test
    void whenPreferences_thenGdxPreferencesReturned() {
        properties.setProperty("preferences.name", "preferences");
        Gdx.app = mock();
        when(Gdx.app.getPreferences("preferences")).thenReturn(preferences);
        assertThat(configuration.preferences()).isEqualTo(preferences);
    }

    @Test
    void whenWorldDimensions_thenParsedFromProperties() {
        properties.setProperty("world.width", "20");
        properties.setProperty("world.height", "15");
        assertThat(configuration.worldDimensions())
            .isEqualTo(new WorldDimensions(20, 15));
    }

    @Test
    void whenInterfaceViewportHeight_thenParsedFromProperties() {
        properties.setProperty("interface.viewport.height", "100.5");
        assertThat(configuration.interfaceViewportHeight()).isEqualTo(100.5f);
    }

    @Test
    void whenSnakeFoodGrowth_thenParsedFromProperties() {
        properties.setProperty("snake.food.growth", "3");
        assertThat(configuration.snakeFoodGrowth()).isEqualTo(3);
    }

    @Test
    void whenSnakeHeadPosition_thenParsedFromProperties() {
        properties.setProperty("snake.head.x", "5");
        properties.setProperty("snake.head.y", "8");
        assertThat(configuration.snakeHeadPosition()).isEqualTo(new Position(5, 8));
    }

    @Test
    void whenSnakeHeadDirection_thenParsedFromProperties() {
        properties.setProperty("snake.head.direction", "LEFT");
        assertThat(configuration.snakeHeadDirection()).isEqualTo(Direction.LEFT);
    }

    @Test
    void whenSnakeLength_thenParsedFromProperties() {
        properties.setProperty("snake.length", "4");
        assertThat(configuration.snakeLength()).isEqualTo(4);
    }

    @Test
    void whenAssets_thenParsedFromProperties() {
        properties.setProperty("assets.skin", "skin");
        properties.setProperty("assets.font.atlas.name", "font atlas");
        properties.setProperty("assets.bundle.loading", "loading");
        properties.setProperty("assets.font.name", "font");
        properties.setProperty("assets.bundle.game", "game");
        var bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "font atlas";
        assertThat(configuration.assets())
            .usingRecursiveComparison()
            .isEqualTo(
                new Assets(
                    new AssetDescriptor<>("skin", Skin.class),
                    new AssetDescriptor<>("loading", I18NBundle.class),
                    new AssetDescriptor<>("font", BitmapFont.class, bitmapFontParameter),
                    new AssetDescriptor<>("game", I18NBundle.class)
                )
            );
    }

    @Test
    void whenModes_thenModesParsedWithRequirementsAndPalette() {
        properties.setProperty("modes", "first,second");
        properties.setProperty("modes.first.game.interval", "0.1");
        properties.setProperty("modes.first.snake.turns.legal", "LEFT,RIGHT");
        properties.setProperty("modes.first.palette.BACKGROUND", "FFFFFFFF");
        properties.setProperty("modes.first.palette.HEAD", "FFFFFFFF");
        properties.setProperty("modes.first.palette.SEGMENT", "FFFFFFFF");
        properties.setProperty("modes.first.palette.FOOD", "FFFFFFFF");
        properties.setProperty("modes.first.palette.FOOD_EATEN_COUNTER", "FFFFFFFF");
        properties.setProperty("modes.first.unlock.requirements.profile.LAUNCHES", "1");
        properties.setProperty("modes.second.game.interval", "0.2");
        properties.setProperty("modes.second.snake.turns.legal", "RIGHT");
        properties.setProperty("modes.second.palette.BACKGROUND", "FFFFFFFF");
        properties.setProperty("modes.second.palette.HEAD", "FFFFFFFF");
        properties.setProperty("modes.second.palette.SEGMENT", "FFFFFFFF");
        properties.setProperty("modes.second.palette.FOOD", "FFFFFFFF");
        properties.setProperty("modes.second.palette.FOOD_EATEN_COUNTER", "FFFFFFFF");
        properties.setProperty("modes.second.unlock.requirements.session.LEFT_TURNS", "1");
        var color = new Color(0xffffffff);
        var palette = Map.of(
            Colored.BACKGROUND, color,
            Colored.HEAD, color,
            Colored.SEGMENT, color,
            Colored.FOOD, color,
            Colored.FOOD_EATEN_COUNTER, color
        );
        assertThat(configuration.modes(userProfile))
            .containsExactly(
                new Mode(
                    "first",
                    0.1f,
                    Set.of(LegalTurn.LEFT, LegalTurn.RIGHT),
                    palette,
                    new ModeUnlockRequirements(
                        userProfile,
                        Map.of(
                            UserProfileStatistics.LAUNCHES, 1
                        ),
                        Map.of(
                            SessionStatistics.LEFT_TURNS, 0
                        )
                    )
                ),
                new Mode(
                    "second",
                    0.2f,
                    Set.of(LegalTurn.RIGHT),
                    palette,
                    new ModeUnlockRequirements(
                        userProfile,
                        Map.of(
                            UserProfileStatistics.LAUNCHES, 0
                        ),
                        Map.of(
                            SessionStatistics.LEFT_TURNS, 1
                        )
                    )
                )
            );
    }
}
