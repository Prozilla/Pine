package dev.prozilla.pine.common.math.vector;

/**
 * 2-dimensional anchor point relative to the bottom-left corner of a square with width and height <code>1f</code>.
 */
public enum GridAlignment {
	TOP_LEFT(0, 1),
	TOP(0.5f, 1),
	TOP_RIGHT(1, 1),
	LEFT(0, 0.5f),
	CENTER(0.5f, 0.5f),
	RIGHT(1, 0.5f),
	BOTTOM_LEFT(0, 0),
	BOTTOM(0.5f, 0),
	BOTTOM_RIGHT(1, 0);
	
	/** Horizontal factor of this alignment. Range from 0f to 1f. */
	public final float x;
	/** Vertical factor of this alignment. Range from 0f to 1f. */
	public final float y;
	
	GridAlignment(float x, float y) {
		this.x = x;
		this.y = y;
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
	
}
