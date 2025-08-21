package dev.prozilla.pine.common.asset.text;

/**
 * Represents a font glyph.
 */
public class Glyph {
	
	// Texture region
	public final float regionWidth;
	public final float regionHeight;
	public final float regionX;
	public final float regionY;
	
	// Positioning
	public final float y;
	public final float height;
	public final float advance;
	
	/**
	 * Creates a font Glyph.
	 * @param regionWidth Width of the Glyph
	 * @param regionHeight Height of the Glyph
	 * @param regionX X coordinate on the font texture
	 * @param regionY Y coordinate on the font texture
	 * @param y Vertical offset from the baseline
	 * @param advance Advance width
	 */
	public Glyph(float regionWidth, float regionHeight, float regionX, float regionY, float y, float height, float advance) {
		this.regionWidth = regionWidth;
		this.regionHeight = regionHeight;
		this.regionX = regionX;
		this.regionY = regionY;
		this.y = y;
		this.height = height;
		this.advance = advance;
	}
	
}
