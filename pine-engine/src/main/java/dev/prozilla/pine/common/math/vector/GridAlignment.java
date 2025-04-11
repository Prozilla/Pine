package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.util.EnumUtils;

/**
 * 2-dimensional anchor point relative to the bottom-left corner of a square with width and height <code>1f</code>.
 */
public enum GridAlignment {
	TOP_LEFT(0, 1, "top-left"),
	TOP(0.5f, 1, "top"),
	TOP_RIGHT(1, 1, "top-right"),
	LEFT(0, 0.5f, "left"),
	CENTER(0.5f, 0.5f, "center"),
	RIGHT(1, 0.5f, "right"),
	BOTTOM_LEFT(0, 0, "bottom-left"),
	BOTTOM(0.5f, 0, "bottom"),
	BOTTOM_RIGHT(1, 0, "bottom-right");
	
	/** Horizontal factor of this alignment. Range from 0f to 1f. */
	public final float x;
	/** Vertical factor of this alignment. Range from 0f to 1f. */
	public final float y;
	
	private final String string;
	
	GridAlignment(float x, float y, String string) {
		this.x = x;
		this.y = y;
		this.string = string;
	}
	
	/**
	 * Converts this alignment to a vector inside a rectangle.
	 * @param width Width of the rectangle
	 * @param height Height of the rectangle
	 * @return The position inside the rectangle as a {@link Vector2f}.
	 */
	public Vector2f toVector(float width, float height) {
		Vector2f vector = toVector();
		vector.x *= width;
		vector.y *= height;
		return vector;
	}
	
	/**
	 * @return This alignment as a vector.
	 */
	public Vector2f toVector() {
		return new Vector2f(x, y);
	}
	
	@Override
	public String toString() {
		return string;
	}
	
	public static GridAlignment parse(String input) {
		return EnumUtils.findByName(GridAlignment.values(), input);
	}
	
}
