package com.github.maximtereshchenko.games.snake;

import java.util.Locale;

enum Mode {

    CLASSIC {
        @Override
        boolean isLegal(Direction current, Direction next) {
            return next != current.opposite();
        }
    },
    VIPER {
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

    abstract boolean isLegal(Direction current, Direction next);

    private String property(String suffix) {
        return "mode.%s.%s".formatted(this, suffix);
    }
}
