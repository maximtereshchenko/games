package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

import java.util.stream.Stream;

final class BuildingsCountStatisticsLabel extends StatisticsValueLabel {

    BuildingsCountStatisticsLabel(
        Skin skin,
        BakeryService bakeryService
    ) {
        super(skin, bakeryService);
    }

    @Override
    String text(BakeryService bakeryService) {
        return String.valueOf(
            Stream.of(Building.values())
                .mapToInt(bakeryService::count)
                .sum()
        );
    }
}
