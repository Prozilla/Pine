package dev.prozilla.pine.common.asset;

import dev.prozilla.pine.common.math.vector.Vector2i;

/**
 * Packs rectangles into rows of a fixed-size area, left to right and top to bottom.
 */
public class RectPacker {
	
	private final int width;
	private final int height;
	private final int margin;
	
	private int cursorX;
	private int cursorY;
	private int rowHeight;
	
	/**
	 * @param width Width of the area
	 * @param height Height of the area
	 */
	public RectPacker(int width, int height) {
		this(width, height, 0);
	}
	
	/**
	 * @param width Width of the area
	 * @param height Height of the area
	 * @param margin Empty space between rectangles and around the edges of the area
	 */
	public RectPacker(int width, int height, int margin) {
		this.width = width;
		this.height = height;
		this.margin = margin;
		cursorX = margin;
		cursorY = margin;
	}
	
	/**
	 * Reserves space for a rectangle.
	 * @return The top left position of the reserved space, or <code>null</code> if the rectangle does not fit.
	 */
	public Vector2i pack(int width, int height) {
		if (width + margin * 2 > this.width) {
			return null;
		}
		
		if (cursorX + width + margin > this.width) {
			cursorX = margin;
			cursorY += rowHeight + margin;
			rowHeight = 0;
		}
		if (cursorY + height + margin > this.height) {
			return null;
		}
		
		Vector2i position = new Vector2i(cursorX, cursorY);
		cursorX += width + margin;
		rowHeight = Math.max(rowHeight, height);
		return position;
	}
	
}