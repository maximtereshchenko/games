package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;

import java.time.Clock;

final class GeneralStatisticsPanel extends Table {

    GeneralStatisticsPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Clock clock
    ) {
        var icon = skin.get(Style.class).icon;
        defaults().left().padBottom(4);
        addLine(
            skin,
            bundle.get("statistics.balance"),
            new Image(icon),
            new BalanceStatisticsLabel(
                skin,
                bigDecimalFormatter,
                bakeryService
            )
        );
        addLine(
            skin,
            bundle.get("statistics.cumulative-baked"),
            new Image(icon),
            new CumulativeBakedStatisticsLabel(
                skin,
                bigDecimalFormatter,
                bakeryService
            )
        );
        addLine(
            skin,
            bundle.get("statistics.time-passed"),
            new TimePassedStatisticsLabel(
                skin,
                bundle,
                bakeryService,
                clock
            )
        );
        addLine(
            skin,
            bundle.get("statistics.buildings-count"),
            new BuildingsCountStatisticsLabel(
                skin,
                bakeryService
            )
        );
        addLine(
            skin,
            bundle.get("statistics.baking-rate"),
            new Image(icon),
            new BakingRateStatisticsLabel(
                skin,
                bigDecimalFormatter,
                bakeryService
            )
        );
        addLine(
            skin,
            bundle.get("statistics.baking-power"),
            new Image(icon),
            new BakingPowerStatisticsLabel(
                skin,
                bigDecimalFormatter,
                bakeryService
            )
        );
        addLine(
            skin,
            bundle.get("statistics.cumulative-clicks"),
            new CumulativeClicksStatisticsLabel(
                skin,
                bakeryService
            )
        );
        addLine(
            skin,
            bundle.get("statistics.cumulative-manually-baked"),
            new Image(icon),
            new CumulativeManuallyBakedStatisticsLabel(
                skin,
                bigDecimalFormatter,
                bakeryService
            )
        );
    }

    private void addLine(
        Skin skin,
        String keyText,
        Actor... actors
    ) {
        var line = new Table();
        line.defaults().padRight(4);
        line.add(keyLabel(skin, keyText));
        for (var actor : actors) {
            line.add(actor);
        }
        add(line).row();
    }

    private Label keyLabel(Skin skin, String text) {
        return new Label(
            text,
            skin,
            "statistics-key"
        );
    }

    private static final class Style {

        Drawable icon;
    }
}
