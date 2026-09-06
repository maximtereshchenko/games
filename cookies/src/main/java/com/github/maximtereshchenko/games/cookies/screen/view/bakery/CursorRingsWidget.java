package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

import java.util.ArrayList;
import java.util.List;

final class CursorRingsWidget extends WidgetGroup {

    private final Skin skin;
    private final BakeryService bakeryService;
    private final List<CursorWidget> cursorWidgets;
    private double accumulatedTimeSeconds;

    CursorRingsWidget(Skin skin, BakeryService bakeryService) {
        this.skin = skin;
        this.bakeryService = bakeryService;
        this.cursorWidgets = new ArrayList<>();
        setLayoutEnabled(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        accumulatedTimeSeconds += delta;
        addCursors();
        transformCursors();
    }

    @Override
    public void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
        for (var cursor : cursorWidgets) {
            cursor.scale(getWidth());
        }
    }

    private void transformCursors() {
        var cursorsPerRing = 50;
        for (var i = 0; i < cursorWidgets.size(); i++) {
            cursorWidgets.get(i)
                .transform(
                    i / cursorsPerRing,
                    i % cursorsPerRing,
                    cursorsPerRing,
                    360f / cursorsPerRing,
                    accumulatedTimeSeconds,
                    getOriginX(),
                    getOriginY()
                );
        }
    }

    private void addCursors() {
        for (
            var i = cursorWidgets.size();
            i < bakeryService.count(Building.CURSOR);
            i++
        ) {
            var cursorWidget = new CursorWidget(skin);
            cursorWidget.scale(getWidth());
            cursorWidgets.add(cursorWidget);
            addActor(cursorWidget);
        }
    }
}