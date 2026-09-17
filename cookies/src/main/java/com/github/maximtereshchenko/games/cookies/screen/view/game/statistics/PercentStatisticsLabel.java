package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.util.stream.Stream;

abstract class PercentStatisticsLabel<T extends Enum<T>> extends StatisticsValueLabel {

    private final I18NBundle bundle;
    private final String key;
    private final T[] values;

    PercentStatisticsLabel(
        AssetManager assetManager,
        Assets assets,
        String key,
        BakeryService bakeryService,
        T[] values
    ) {
        super(assetManager, assets, bakeryService);
        this.bundle = assetManager.get(assets.game().bundle());
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
