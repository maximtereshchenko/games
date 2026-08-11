package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class ScaledFontTest {

    private final BitmapFont bitmapFont = mock();
    private final BitmapFont.BitmapFontData bitmapFontData = mock();
    private final ScaledFont scaledFont = new ScaledFont(bitmapFont);

    @BeforeEach
    void setUp() {
        when(bitmapFont.getData()).thenReturn(bitmapFontData);
        bitmapFontData.scaleX = 1.5f;
        bitmapFontData.scaleY = 2.5f;
    }

    @Test
    void whenUse_thenScaledDuringConsumerAndRestoredAfter() {
        var fontPassedToConsumer = new AtomicReference<BitmapFont>();
        scaledFont.use(
            3,
            font -> {
                verify(bitmapFontData).setScale(3);
                verify(bitmapFontData, never()).setScale(1.5f, 2.5f);
                fontPassedToConsumer.set(font);
            }
        );
        assertThat(fontPassedToConsumer.get()).isSameAs(bitmapFont);
        verify(bitmapFontData).setScale(1.5f, 2.5f);
    }
}
