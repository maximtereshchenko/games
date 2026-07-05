package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Screen;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

final class LazyScreenTest {

    private final Supplier<Screen> supplier = mock();
    private final Screen screen = mock();
    private final LazyScreen lazyScreen = new LazyScreen(supplier);

    private static Stream<Consumer<Screen>> methods() {
        return Stream.of(
            Screen::show,
            screen -> screen.render(1),
            screen -> screen.resize(1, 2),
            Screen::pause,
            Screen::resume,
            Screen::hide,
            Screen::dispose
        );
    }

    @ParameterizedTest
    @MethodSource("methods")
    void givenSupplier_thenScreenLazilyCreated(Consumer<Screen> method) {
        when(supplier.get()).thenReturn(screen);
        method.accept(lazyScreen);
        method.accept(lazyScreen);
        verify(supplier).get();
        method.accept(verify(screen, times(2)));
    }
}