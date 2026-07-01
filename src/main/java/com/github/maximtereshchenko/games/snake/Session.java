package com.github.maximtereshchenko.games.snake;

final class Session {

    Status status;

    Session(Status status) {
        this.status = status;
    }

    Session() {
        this(Status.RUNNING);
    }

    enum Status {

        RUNNING, ENDED
    }
}
