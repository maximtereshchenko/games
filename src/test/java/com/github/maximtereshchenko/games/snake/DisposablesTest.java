package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.utils.Disposable;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class DisposablesTest {

    private final Disposable disposable = mock();
    private final Disposables disposables = new Disposables();

    @Test
    void givenDisposableAdded_thenDisposedOnDispose() {
        disposables.add(disposable);
        disposables.dispose();
        verify(disposable).dispose();
    }
}
