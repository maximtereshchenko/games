package com.github.maximtereshchenko.games.snake;

import java.util.Locale;

enum Mode {

    CLASSIC, VIPER;

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

    private String property(String suffix) {
        return "mode.%s.%s".formatted(this, suffix);
    }
}
