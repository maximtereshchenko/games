package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;

import java.util.Locale;
import java.util.Map;

enum Mode {

    CLASSIC {
        @Override
        Map<Colored, Color> palette() {
            return Map.of(
                Colored.BACKGROUND, Color.TAN,
                Colored.HEAD, Color.FOREST,
                Colored.SEGMENT, Color.OLIVE,
                Colored.FOOD, Color.FOREST,
                Colored.FOOD_EATEN_COUNTER, Color.GRAY
            );
        }

        @Override
        boolean isLegal(Direction current, Direction next) {
            return next != current.opposite();
        }
    },
    VIPER {
        @Override
        Map<Colored, Color> palette() {
            return Map.of(
                Colored.BACKGROUND, Color.TAN,
                Colored.HEAD, Color.BROWN,
                Colored.SEGMENT, Color.GOLDENROD,
                Colored.FOOD, Color.BROWN,
                Colored.FOOD_EATEN_COUNTER, Color.ROYAL
            );
        }

        @Override
        boolean isLegal(Direction current, Direction next) {
            return next == current.right();
        }
    };

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ROOT);
    }

    String nameKey() {
        return property("name");
    }

    String descriptionKey() {
        return property("description");
    }

    String requirementKey() {
        return property("requirement");
    }

    abstract Map<Colored, Color> palette();

    abstract boolean isLegal(Direction current, Direction next);

    private String property(String suffix) {
        return "mode.%s.%s".formatted(this, suffix);
    }
}
