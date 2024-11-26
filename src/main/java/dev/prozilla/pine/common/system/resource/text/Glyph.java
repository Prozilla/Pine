package dev.prozilla.pine.common.system.resource.text;

/**
 * Represents a font glyph.
 */
public class Glyph {

    public final int width;
    public final int height;
    public final int x;
    public final int y;
    public final float advance;

    /**
     * Creates a font Glyph.
     * @param width Width of the Glyph
     * @param height Height of the Glyph
     * @param x X coordinate on the font texture
     * @param y Y coordinate on the font texture
     * @param advance Advance width
     */
    public Glyph(int width, int height, int x, int y, float advance) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.advance = advance;
    }
}
