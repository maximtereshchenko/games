package com.github.maximtereshchenko.games.snakes.screen.view.main;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

public final class MainView extends Table {

    public MainView(
        ModesView modesView,
        NavigationView navigationView,
        InformationView informationView
    ) {
        var table = new Table();
        table.defaults().growX();
        table.add(modesView).row();
        table.add(navigationView);
        add(table).width(Value.percentWidth(0.6f, this));
        add(informationView).growX();
    }
}
