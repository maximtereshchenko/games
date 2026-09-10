package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.util.stream.Stream;

abstract class PercentStatisticsLabel<T extends Enum<T>> extends StatisticsValueLabel {

    private final I18NBundle bundle;
    private final String key;
    private final T[] values;

    PercentStatisticsLabel(
        Skin skin,
        I18NBundle bundle,
        String key,
        BakeryService bakeryService,
        T[] values
    ) {
        super(skin, bakeryService);
        this.bundle = bundle;
        this.key = key;
        this.values = values;
    }

    @Override
    final String text(BakeryService bakeryService) {
        var count = Stream.of(values)
            .filter(value -> isCounted(bakeryService, value))
            .count();
        return bundle.format(
            key,
            count,
            values.length,
            (float) count / values.length
        );
    }

    abstract boolean isCounted(BakeryService bakeryService, T value);
}
