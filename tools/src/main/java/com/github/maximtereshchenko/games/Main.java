package com.github.maximtereshchenko.games;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.Json;

final class Main {

    static void main(String[] args) {
        GdxNativesLoader.load();
        switch (args[0]) {
            case "generateBitmapFont" -> generateBitmapFont(
                args[1],
                args[2],
                args[3]
            );
            case "slice" -> slice(
                args[1],
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                args[4]
            );
            case "generateEmptyPixel" -> generateEmptyPixel(
                args[1]
            );
            case "generateLinearGradient" -> generateLinearGradient(
                args[1]
            );
            case "generateEmptyCircle" -> generateEmptyCircle(
                args[1]
            );
            case "generateTextureAtlas" -> generateTextureAtlas(
                args[1],
                args[2],
                args[3]
            );
            case "applyBordersGradient" -> applyBordersGradient(
                args[1],
                args[2],
                args[3]
            );
        }
    }

    private static void generateTextureAtlas(
        String inputDirectoryPath,
        String outputDirectoryPath,
        String fileName
    ) {
        TexturePacker.process(
            inputDirectoryPath,
            outputDirectoryPath,
            fileName
        );
    }

    private static void generateLinearGradient(
        String outputPath
    ) {
        var pixmap = new Pixmap(203, 3, Pixmap.Format.RGBA8888);
        for (int x = 1; x < pixmap.getWidth() - 1; x++) {
            var progress = (float) (x - 1) / (pixmap.getWidth() - 3);
            float alpha;
            if (progress <= 0.5f) {
                alpha = MathUtils.lerp(0.0f, 0.25f, progress * 2f);
            } else {
                alpha = MathUtils.lerp(0.25f, 0.0f, (progress - 0.5f) * 2f);
            }
            pixmap.drawPixel(x, 1, Color.rgba8888(1f, 1f, 1f, alpha));
        }
        pixmap.drawPixel(pixmap.getWidth() / 2, 0, Color.rgba8888(0, 0, 0, 1));
        PixmapIO.writePNG(new FileHandle(outputPath), pixmap);
    }

    private static void generateEmptyCircle(
        String outputPath
    ) {
        var pixmap = new Pixmap(11, 11, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, pixmap.getWidth() / 2 - 1);
        pixmap.drawPixel(pixmap.getWidth() / 2, 0, Color.rgba8888(0, 0, 0, 1));
        pixmap.drawPixel(0, pixmap.getHeight() / 2, Color.rgba8888(0, 0, 0, 1));
        PixmapIO.writePNG(new FileHandle(outputPath), pixmap);
    }

    private static void generateEmptyPixel(String path) {
        var pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        PixmapIO.writePNG(
            new FileHandle(path),
            pixmap
        );
    }

    private static void slice(String imagePath, int width, int height, String directoryPath) {
        var imageFile = new FileHandle(imagePath);
        var fullPixmap = new Pixmap(new FileHandle(imagePath));
        var index = 0;
        for (var row = 0; row < fullPixmap.getHeight(); row += height) {
            for (var column = 0; column < fullPixmap.getWidth(); column += width) {
                var pixmap = new Pixmap(width, height, fullPixmap.getFormat());
                pixmap.drawPixmap(
                    fullPixmap,
                    column, row, width, height,
                    0, 0, width, height
                );
                if (!isPixmapBlank(pixmap)) {
                    PixmapIO.writePNG(
                        new FileHandle(
                            "%s/%s%d.%s".formatted(
                                directoryPath,
                                imageFile.nameWithoutExtension(),
                                index++,
                                imageFile.extension()
                            )
                        ),
                        pixmap
                    );
                }
            }
        }
    }

    private static boolean isPixmapBlank(Pixmap pixmap) {
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                int pixel = pixmap.getPixel(x, y);
                if ((pixel & 0x000000ff) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void generateBitmapFont(
        String fontPath,
        String freeTypeFontParameterJson,
        String directoryPath
    ) {
        var configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setInitialVisible(false);
        new Lwjgl3Application(
            new ApplicationListener() {

                @Override
                public void create() {}

                @Override
                public void resize(int width, int height) {}

                @Override
                public void render() {
                    var fontFile = Gdx.files.absolute(fontPath);
                    var generator = new FreeTypeFontGenerator(fontFile);
                    var freeTypeFontParameter = new Json()
                        .fromJson(
                            FreeTypeFontGenerator.FreeTypeFontParameter.class,
                            freeTypeFontParameterJson
                        );
                    var font = generator.generateFont(freeTypeFontParameter);
                    var regions = font.getRegions();
                    var pixmaps = new Pixmap[regions.size];
                    for (int i = 0; i < regions.size; i++) {
                        var texture = regions.get(i).getTexture();
                        var textureData = texture.getTextureData();
                        if (!textureData.isPrepared()) {
                            textureData.prepare();
                        }
                        pixmaps[i] = textureData.consumePixmap();
                    }
                    var info = new BitmapFontWriter.FontInfo();
                    info.overrideMetrics(font.getData());
                    info.face = fontFile.nameWithoutExtension();
                    info.size = freeTypeFontParameter.size;
                    info.descent = 0;
                    BitmapFontWriter.writeFont(
                        font.getData(),
                        pixmaps,
                        Gdx.files.absolute(
                            "%s/%s.fnt".formatted(
                                directoryPath,
                                fontFile.nameWithoutExtension()
                            )
                        ),
                        info
                    );
                    Gdx.app.exit();
                }

                @Override
                public void pause() {}

                @Override
                public void resume() {}

                @Override
                public void dispose() {}
            },
            configuration
        );
    }

    private static void applyBordersGradient(String imagePath, String colorJson, String outputPath) {
        var blurRadius = 16;
        var sourcePixmap = new Pixmap(new FileHandle(imagePath));
        var blackMask = generateGradientMask(sourcePixmap, blurRadius);
        var blurredBlackMask = applyBlur(blackMask, blurRadius / 2);
        var blackResult = compositeGradient(
            sourcePixmap,
            blurredBlackMask,
            new Json().fromJson(Color.class, colorJson)
        );
        PixmapIO.writePNG(new FileHandle(outputPath), blackResult);
    }

    private static Pixmap generateGradientMask(
        Pixmap sourcePixmap,
        int blurRadius
    ) {
        var width = sourcePixmap.getWidth();
        var height = sourcePixmap.getHeight();
        var mask = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        mask.setBlending(Pixmap.Blending.None);
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var color = sourcePixmap.getPixel(x, y);
                var alpha = color & 0x000000FF;
                if (alpha > 0) {
                    var distToEdgeX = Math.min(x, width - 1 - x);
                    var distToEdgeY = Math.min(y, height - 1 - y);
                    var minEdgeDist = Math.min(distToEdgeX, distToEdgeY);
                    if (minEdgeDist < blurRadius) {
                        var progress = (float) minEdgeDist / blurRadius;
                        var factor = 0.6f * (1.0f - progress);
                        var shadowAlpha = (int) (factor * 255);
                        mask.drawPixel(x, y, shadowAlpha);
                    }
                }
            }
        }
        return mask;
    }

    private static Pixmap compositeGradient(
        Pixmap sourcePixmap,
        Pixmap shadowAlphaMask,
        Color color
    ) {
        var width = sourcePixmap.getWidth();
        var height = sourcePixmap.getHeight();
        var result = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        result.setBlending(Pixmap.Blending.None);
        result.drawPixmap(sourcePixmap, 0, 0);
        result.setBlending(Pixmap.Blending.SourceOver);
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var shadowAlpha = shadowAlphaMask.getPixel(x, y) & 0x000000FF;
                if (shadowAlpha > 0) {
                    var pixelColor = Color.rgba8888(color) | shadowAlpha;
                    result.drawPixel(x, y, pixelColor);
                }
            }
        }
        return result;
    }

    private static Pixmap applyBlur(Pixmap src, int radius) {
        var w = src.getWidth();
        var h = src.getHeight();
        var horizontalPass = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        horizontalPass.setBlending(Pixmap.Blending.None);
        var finalPass = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        finalPass.setBlending(Pixmap.Blending.None);
        for (var y = 0; y < h; y++) {
            for (var x = 0; x < w; x++) {
                var alphaSum = 0;
                var count = 0;
                for (var k = -radius; k <= radius; k++) {
                    var sampleX = x + k;
                    if (sampleX >= 0 && sampleX < w) {
                        alphaSum += (src.getPixel(sampleX, y) & 0x000000FF);
                        count++;
                    }
                }
                horizontalPass.drawPixel(x, y, alphaSum / count);
            }
        }
        for (var x = 0; x < w; x++) {
            for (var y = 0; y < h; y++) {
                var alphaSum = 0;
                var count = 0;
                for (var k = -radius; k <= radius; k++) {
                    var sampleY = y + k;
                    if (sampleY >= 0 && sampleY < h) {
                        alphaSum += (horizontalPass.getPixel(x, sampleY) & 0x000000FF);
                        count++;
                    }
                }
                finalPass.drawPixel(x, y, alphaSum / count);
            }
        }
        return finalPass;
    }
}
