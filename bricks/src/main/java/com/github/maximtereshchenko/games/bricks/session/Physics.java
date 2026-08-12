package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;

final class Physics {

    Fixture fixture;

    Physics(Fixture fixture) {
        this.fixture = fixture;
    }
}
