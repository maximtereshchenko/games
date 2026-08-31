package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

final class CookiePanel extends Container<Stack> {

    CookiePanel(Skin skin, Random random) {
        super(new Stack());
        fill();
        clip();
        getActor().add(background(skin));
        getActor().add(new CookieShowerWidget(skin));
        getActor().add(new DynamicCookieShower(skin, random));
        getActor().add(cookieContainer(skin, random));
        getActor().add(cookieTable(skin));
        getActor().add(milkTable(skin));
    }

    private Table background(Skin skin) {
        var table = new Table();
        table.add(
                new Image(
                    skin.getDrawable("gradient_bottom_long")
                )
            )
            .grow()
            .height(Value.percentHeight(0.6f, this))
            .bottom();
        return table;
    }

    private Container<CookieButtonStack> cookieContainer(Skin skin, Random random) {
        var container = new Container<>(new CookieButtonStack(skin, random));
        container.background(skin.getDrawable("gradient_borders"));
        container.size(Value.percentWidth(0.4f, container));
        return container;
    }

    private Table cookieTable(Skin skin) {
        var cookieAmount = new Label("123,456", skin, "label_cookies");
        cookieAmount.setAlignment(Align.center);
        var cookies = new Label("cookies", skin, "label_cookies");
        cookies.setAlignment(Align.center);
        var cookiesPerSecond = new Label("per second: 123", skin, "label_cookiesPerSecond");
        cookiesPerSecond.setAlignment(Align.center);
        var table = new Table();
        table.background(skin.getDrawable("tile_black_transparent40"));
        table.defaults().pad(4).growX();
        table.add(cookieAmount).row();
        table.add(cookies).row();
        table.add(cookiesPerSecond).row();
        var outerTable = new Table();
        outerTable.add().height(Value.percentHeight(0.1f, outerTable)).row();
        outerTable.add(table).growX();
        outerTable.top();
        return outerTable;
    }

    private Table milkTable(Skin skin) {
        var table = new Table();
        table.add(new FlowingMilkWidget(skin))
            .grow()
            .height(Value.percentHeight(0.1f, table))
            .bottom();
        return table;
    }
}
