package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

final class CookiesSkinLoader extends SkinLoader {

    CookiesSkinLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    protected Skin newSkin(TextureAtlas atlas) {
        return new Skin(atlas) {

            @Override
            protected Json getJsonLoader(FileHandle skinFile) {
                var jsonLoader = super.getJsonLoader(skinFile);
                jsonLoader.setSerializer(
                    TiledDrawable.class,
                    new Json.ReadOnlySerializer<>() {

                        @Override
                        public TiledDrawable read(Json json, JsonValue jsonData, Class type) {
                            var drawable = new TiledDrawable();
                            drawable.setRegion(
                                json.readValue(
                                    "region",
                                    TextureRegion.class,
                                    jsonData
                                )
                            );
                            json.readFields(drawable, jsonData);
                            return drawable;
                        }
                    }
                );
                return jsonLoader;
            }
        };
    }
}
