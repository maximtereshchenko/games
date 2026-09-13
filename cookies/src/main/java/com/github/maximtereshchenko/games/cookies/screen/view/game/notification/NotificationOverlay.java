package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.AchievementTooltipPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;
import com.github.maximtereshchenko.games.cookies.screen.view.game.TopCenterTooltipWidget;
import com.github.maximtereshchenko.games.cookies.screen.view.game.UnlockAchievementAction;

public final class NotificationOverlay extends Table {

    public NotificationOverlay(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        bottom();
        padBottom(12);
        defaults().pad(2);
        var closeButtonPopUpFrame = closeButtonPopUpFrame(skin);
        for (var achievement : Achievement.values()) {
            if (!bakeryService.isUnlocked(achievement)) {
                addAction(
                    new UnlockAchievementAction(
                        bakeryService,
                        achievement,
                        Actions.run(
                            () -> add(
                                notificationPopUpFrame(
                                    skin,
                                    bundle,
                                    bakeryService,
                                    achievement
                                ),
                                closeButtonPopUpFrame
                            )
                        )
                    )
                );
            }
        }
    }

    private PopUpFrame notificationPopUpFrame(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        var popUpFrame = new PopUpFrame(
            skin,
            new NotificationPanel(
                skin,
                bundle,
                bakeryService,
                achievement
            )
        );
        popUpFrame.addListener(
            new TopCenterTooltipWidget(
                new PopUpFrame(
                    skin,
                    new AchievementTooltipPanel(
                        skin,
                        bundle,
                        bakeryService,
                        achievement
                    )
                )
            )
        );
        return popUpFrame;
    }

    private PopUpFrame closeButtonPopUpFrame(Skin skin) {
        var closeButtonPopUpFrame = new PopUpFrame(
            skin,
            new CloseButton(skin)
        );
        closeButtonPopUpFrame.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    clearChildren();
                }
            }
        );
        closeButtonPopUpFrame.addAction(
            Actions.forever(
                Actions.run(
                    () -> closeButtonPopUpFrame.setVisible(
                        getChildren().size > 2
                    )
                )
            )
        );
        return closeButtonPopUpFrame;
    }

    private void add(PopUpFrame notification, PopUpFrame closeButton) {
        var row = add(notification)
            .width(400)
            .getRow();
        notification.addListener(
            new ChangeListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    var iterator = getCells().iterator();
                    while (iterator.hasNext()) {
                        var cell = iterator.next();
                        if (cell.getRow() == row - 1 && cell.getColumn() == 1) {
                            cell.setActor(closeButton);
                        }
                        if (cell.getRow() == row) {
                            removeActor(cell);
                            iterator.remove();
                        }
                    }
                }

                private void removeActor(Cell<?> cell) {
                    var actor = cell.getActor();
                    if (actor != null && actor != closeButton) {
                        actor.remove();
                    }
                }
            }
        );
        add(closeButton)
            .left()
            .row();
    }
}
