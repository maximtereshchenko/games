package com.github.maximtereshchenko.games.snakes.session;

public record WorldDimensions(int width, int height) {

    int space() {
        return width * height;
    }
}
