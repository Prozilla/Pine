package dev.prozilla.pine.common.asset.text;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.*;

/**
 * Contains a font texture for drawing text.
 */
public class Font implements Asset {

    /**
     * Contains the glyphs for each char.
     */
    private final Map<Character, Glyph> glyphs;
    /**
     * Contains the font texture.
     */
    private final Texture texture;

    /**
     * Height of the font.
     */
    private int fontHeight;
    
    private final int size;
    
    public String path;
    
    public final static int DEFAULT_SIZE = 16;
    public final static Color DEFAULT_COLOR = Color.white();

    /**
     * Creates a default anti-aliased font with monospaced glyphs and default
     * size 16.
     */
    public Font() {
        this(new java.awt.Font(MONOSPACED, PLAIN, 16), true);
    }

    /**
     * Creates a default font with monospaced glyphs and default size 16.
     *
     * @param antiAlias Whether the font should be anti-aliased or not
     */
    public Font(boolean antiAlias) {
        this(new java.awt.Font(MONOSPACED, PLAIN, 16), antiAlias);
    }

    /**
     * Creates a default anti-aliased font with monospaced glyphs and specified
     * size.
     *
     * @param size Font size
     */
    public Font(int size) {
        this(new java.awt.Font(MONOSPACED, PLAIN, size), true);
    }

    /**
     * Creates a default font with monospaced glyphs and specified size.
     *
     * @param size      Font size
     * @param antiAlias Whether the font should be anti-aliased or not
     */
    public Font(int size, boolean antiAlias) {
        this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias);
    }

    /**
     * Creates an anti-aliased Font from an input stream.
     *
     * @param in   The input stream
     * @param size Font size
     *
     * @throws FontFormatException if fontFile does not contain the required
     *                             font tables for the specified format
     * @throws IOException         If font can't be read
     */
    public Font(InputStream in, int size) throws FontFormatException, IOException {
        this(in, size, true);
    }

    /**
     * Creates a Font from an input stream.
     *
     * @param in        The input stream
     * @param size      Font size
     * @param antiAlias Whether the font should be anti-aliased or not
     *
     * @throws FontFormatException if fontFile does not contain the required
     *                             font tables for the specified format
     * @throws IOException         If font can't be read
     */
    public Font(InputStream in, int size, boolean antiAlias) throws FontFormatException, IOException {
        this(java.awt.Font.createFont(TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias);
    }

    /**
     * Creates an anti-aliased font from an AWT Font.
     *
     * @param font The AWT Font
     */
    public Font(java.awt.Font font) {
        this(font, true);
    }

    /**
     * Creates a font from an AWT Font.
     *
     * @param font      The AWT Font
     * @param antiAlias Whether the font should be anti-aliased or not
     */
    public Font(java.awt.Font font, boolean antiAlias) {
        glyphs = new HashMap<>();
        texture = createFontTexture(font, antiAlias);
        size = font.getSize();
    }

    /**
     * Creates a font texture from specified AWT font.
     *
     * @param font      The AWT font
     * @param antiAlias Whether the font should be anti-aliased or not
     *
     * @return Font texture
     */
    private Texture createFontTexture(java.awt.Font font, boolean antiAlias) {
        /* Loop through the characters to get charWidth and charHeight */
        int imageWidth = 0;
        int imageHeight = 0;

        /* Start at char #32, because ASCII 0 to 31 are just control codes */
        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                /* ASCII 127 is the DEL control code, so we can skip it */
                continue;
            }
            char c = (char) i;
            BufferedImage ch = createCharImage(font, c, antiAlias);
            if (ch == null) {
                /* If char image is null that font does not contain the char */
                continue;
            }

            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }

        fontHeight = imageHeight;

        /* Image for the texture */
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        int x = 0;

        /* Create image for the standard chars, again we omit ASCII 0 to 31
         * because they are just control codes */
        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                /* ASCII 127 is the DEL control code, so we can skip it */
                continue;
            }
            char c = (char) i;
            BufferedImage charImage = createCharImage(font, c, antiAlias);
            if (charImage == null) {
                /* If char image is null that font does not contain the char */
                continue;
            }

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            /* Create glyph and draw char on image */
            Glyph ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
            g.drawImage(charImage, x, 0, null);
            x += ch.width;
            glyphs.put(c, ch);
        }

        /* Flip image Horizontal to get the origin to bottom left */
        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform,
                                                            AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);

        /* Get charWidth and charHeight of image */
        int width = image.getWidth();
        int height = image.getHeight();

        /* Get pixel data of image */
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        /* Put pixel data into a ByteBuffer */
        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                /* Pixel as RGBA: 0xAARRGGBB */
                int pixel = pixels[i * width + j];
                /* Red component 0xAARRGGBB >> 16 = 0x0000AARR */
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                /* Green component 0xAARRGGBB >> 8 = 0x00AARRGG */
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
                buffer.put((byte) (pixel & 0xFF));
                /* Alpha component 0xAARRGGBB >> 24 = 0x000000AA */
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        /* Do not forget to flip the buffer! */
        buffer.flip();

        /* Create texture */
        Texture fontTexture = new Texture(null, width, height, buffer);
        MemoryUtil.memFree(buffer);
        return fontTexture;
    }

    /**
     * Creates a char image from specified AWT font and char.
     *
     * @param font      The AWT font
     * @param c         The char
     * @param antiAlias Whether the char should be anti-aliased or not
     *
     * @return Char image
     */
    private BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias) {
        /* Creating temporary image to extract character size */
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        /* Get char charWidth and charHeight */
        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        /* Check if charWidth is 0 */
        if (charWidth == 0) {
            return null;
        }

        /* Create image for holding the char */
        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();
        return image;
    }

    /**
     * Gets the width of the specified text.
     *
     * @param text The text
     *
     * @return Width of text
     */
    public int getWidth(CharSequence text) {
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character == '\n') {
                /* Line end, set width to maximum from line width and stored
                 * width */
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (character == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph glyph = glyphs.get(character);
            if (glyph != null) {
                lineWidth += glyph.width;
            }
        }
        width = Math.max(width, lineWidth);
        return width;
    }

    /**
     * Gets the height of the specified text.
     *
     * @param text The text
     *
     * @return Height of text
     */
    public int getHeight(CharSequence text) {
        int height = 0;
        int lineHeight = 0;
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character == '\n') {
                /* Line end, add line height to stored height */
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (character == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph glyph = glyphs.get(character);
            if (glyph != null) {
                lineHeight = Math.max(lineHeight, glyph.height);
            }
        }
        height += lineHeight;
        return height;
    }
    
    public int getSize() {
        return size;
    }
    
    /**
     * Draw text at the specified position and color.
     *
     * @param renderer The renderer to use
     * @param text     TextRenderer to draw
     * @param x        X coordinate of the text position
     * @param y        Y coordinate of the text position
     * @param c        Color to use
     */
    public void drawText(Renderer renderer, CharSequence text, float x, float y, float z, Color c) {
        int textHeight = getHeight(text);

        float drawX = x;
        float drawY = y;
        if (textHeight > fontHeight) {
            drawY += textHeight - fontHeight;
        }

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character == '\n') {
                /* Line feed, set x and y to draw at the next line */
                drawY -= fontHeight;
                drawX = x;
                continue;
            }
            if (character == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph glyph = glyphs.get(character);
            if (glyph != null) {
                renderer.drawTextureRegion(texture, drawX, drawY, z, glyph.x, glyph.y, glyph.width, glyph.height, c);
                drawX += glyph.width;
            }
        }
    }

    /**
     * Draw text at the specified position.
     *
     * @param renderer The renderer to use
     * @param text     TextRenderer to draw
     * @param x        X coordinate of the text position
     * @param y        Y coordinate of the text position
     */
    public void drawText(Renderer renderer, CharSequence text, float x, float y, float z) {
        drawText(renderer, text, x, y, z, DEFAULT_COLOR);
    }
    
    /**
     * Creates a new font from the same font file, but with a different size.
     */
    public Font setSize(int size) {
        return AssetPools.fonts.load(path, size);
    }
    
    @Override
    public String getPath() {
        return path;
    }
    
    /**
     * Deletes the font.
     */
    @Override
    public void destroy() {
        String path = getPath();
        if (path != null) {
            AssetPools.fonts.remove(this);
        }
        texture.destroy();
    }
    
    public static String generateKey(String path, int size) {
        return String.format("%s:%s", path, size);
    }
}
