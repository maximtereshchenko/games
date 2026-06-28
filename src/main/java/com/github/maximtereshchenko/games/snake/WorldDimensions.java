package com.github.maximtereshchenko.games.snake;

record WorldDimensions(int width, int height) {

    int space() {
        return width * height;
    }
}
