package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;

import java.util.List;
import java.util.Optional;

abstract class TooltipPanel extends Table {

    TooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter
    ) {
        add(icon(skin))
            .width(Value.prefWidth);
        add(header(skin, bundle, bigDecimalFormatter))
            .growX()
            .row();
        addSeparator(skin);
        description(skin, bundle)
            .ifPresent(
                description -> add(description)
                    .colspan(2)
                    .left()
                    .padBottom(8)
                    .row()
            );
        flavorText(skin, bundle)
            .ifPresent(
                flavorText -> add(flavorText)
                    .colspan(2)
                    .right()
                    .padBottom(8)
                    .row()
            );
        footer(skin, bundle)
            .ifPresent(footer -> addFooter(skin, footer));
    }

    abstract Image icon(Skin skin);

    abstract Label name(Skin skin, I18NBundle bundle);

    abstract Optional<Table> value(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter
    );

    abstract List<Badge> badges(
        Skin skin,
        I18NBundle bundle
    );

    abstract Optional<Label> description(
        Skin skin,
        I18NBundle bundle
    );

    abstract Optional<FlavorTextLabel> flavorText(
        Skin skin,
        I18NBundle bundle
    );

    abstract Optional<Actor> footer(
        Skin skin,
        I18NBundle bundle
    );

    private void addSeparator(Skin skin) {
        add(new TooltipSeparator(skin))
            .colspan(2)
            .growX()
            .padBottom(8)
            .row();
    }

    private void addFooter(Skin skin, Actor actor) {
        addSeparator(skin);
        add(actor)
            .colspan(2)
            .growX();
    }

    private Table header(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter
    ) {
        var table = new Table();
        table.add(name(skin, bundle))
            .left();
        table.add().growX();
        table.add(
                value(skin, bigDecimalFormatter)
                    .orElse(null)
            )
            .row();
        table.add(badgeLine(skin, bundle))
            .colspan(3)
            .left();
        return table;
    }

    private Table badgeLine(
        Skin skin,
        I18NBundle bundle
    ) {
        var table = new Table();
        for (var badge : badges(skin, bundle)) {
            table.add(badge);
        }
        return table;
    }
}
