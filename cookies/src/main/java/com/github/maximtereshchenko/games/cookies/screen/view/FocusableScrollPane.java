package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

final class FocusableScrollPane<T extends Actor & Layout> extends ScrollPane {

    private final T layout;

    FocusableScrollPane(Skin skin, T layout) {
        super(layout, skin);
        this.layout = layout;
        setScrollbarsOnTop(true);
        addListener(
            new InputListener() {

                @Override
                public void enter(
                    InputEvent event,
                    float x,
                    float y,
                    int pointer,
                    Actor fromActor
                ) {
                    setScrollFocus(
                        pointer,
                        FocusableScrollPane.this
                    );
                }

                @Override
                public void exit(
                    InputEvent event,
                    float x,
                    float y,
                    int pointer,
                    Actor toActor
                ) {
                    setScrollFocus(
                        pointer,
                        null
                    );
                }

                private void setScrollFocus(
                    int pointer,
                    ScrollPane scrollPane
                ) {
                    if (pointer == -1) {
                        getStage().setScrollFocus(scrollPane);
                    }
                }
            }
        );
    }

    @Override
    public float getPrefWidth() {
        return layout.getPrefWidth();
    }
}
