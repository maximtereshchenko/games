package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

final class CookiesLabel extends BaseCookiesLabel {

    CookiesLabel(Skin skin, I18NBundle bundle) {
        super(bundle.get("cookies.name"), skin);
    }
}
