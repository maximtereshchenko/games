package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.utils.Disposable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class Disposables implements Disposable {

    private final Set<Disposable> set = new HashSet<>();

    @Override
    public void dispose() {
        set.forEach(Disposable::dispose);
    }

    void add(Disposable... disposables) {
        Collections.addAll(set, disposables);
    }
}
