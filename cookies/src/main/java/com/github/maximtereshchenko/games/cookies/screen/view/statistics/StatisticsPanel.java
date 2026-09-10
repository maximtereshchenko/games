package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;

import java.time.Clock;

public final class StatisticsPanel extends Container<Table> {

    public StatisticsPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Clock clock
    ) {
        super(new Table());
        fill();
        var style = skin.get(Style.class);
        background(style.bottomBackground);
        var table = getActor();
        table.background(style.topBackground);
        table.pad(6);
        table.top();
        addTitle(skin, bundle, table, style);
        table.add().pad(12).row();
        addPanel(
            skin,
            table,
            style,
            bundle.get("statistics.sub-title.general"),
            new GeneralStatisticsPanel(
                skin,
                bundle,
                bigDecimalFormatter,
                bakeryService,
                clock
            )
        );
        addPanel(
            skin,
            table,
            style,
            bundle.get("statistics.sub-title.upgrades"),
            new ActiveUpgradePanel(
                skin,
                bundle,
                bakeryService
            )
        );
    }

    private void addPanel(
        Skin skin,
        Table table,
        Style style,
        String subTitle,
        Table panel
    ) {
        addSubTitle(
            skin,
            table,
            style,
            subTitle
        );
        table.add(panel)
            .colspan(4)
            .pad(12)
            .growX()
            .row();
    }

    private void addSubTitle(Skin skin, Table table, Style style, String subTitle) {
        addSubTitleSeparator(table, style);
        table.add(
                new Label(
                    subTitle,
                    skin,
                    "statistics-sub-title"
                )
            )
            .pad(12)
            .colspan(2)
            .left()
            .row();
        addSubTitleSeparator(table, style);
    }

    private void addSubTitleSeparator(Table table, Style style) {
        table.add(new Image(style.subTitleSeparator))
            .growX()
            .colspan(2)
            .row();
    }

    private void addTitle(Skin skin, I18NBundle bundle, Table table, Style style) {
        addTitleSeparator(table, style);
        table.add(
                new Label(
                    bundle.get("statistics.title"),
                    skin,
                    "statistics-title"
                )
            )
            .pad(12)
            .colspan(4)
            .row();
        addTitleSeparator(table, style);
    }

    private void addTitleSeparator(Table table, Style style) {
        table.add().growX();
        table.add(new Image(style.titleSeparator))
            .growX()
            .colspan(2);
        table.add().growX().row();
    }

    private static final class Style {

        Drawable bottomBackground;
        Drawable topBackground;
        Drawable titleSeparator;
        Drawable subTitleSeparator;
    }
}
