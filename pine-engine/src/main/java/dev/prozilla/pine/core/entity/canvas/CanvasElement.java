package dev.prozilla.pine.core.entity.canvas;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;

/**
 * A base game object for elements of the user interface.
 * Uses the {@link RectTransform} component.
 */
public class CanvasElement extends Entity {
	
	private RectTransform rectTransform;
	
	public CanvasElement(World world) {
		super(world);
	}
	
	@Override
	public String getName() {
		return getName("CanvasElement");
	}
	
	/**
	 * Sets the position of this element on the canvas.
	 * @param x Horizontal offset
	 * @param y Vertical offset
	 */
	public CanvasElement setPosition(RectTransform.Anchor anchor, int x, int y) {
		if (rectTransform != null) {
			rectTransform.setPosition(anchor, x, y);
		}
		return this;
	}
	
	/**
	 * Sets the anchor of this element on the canvas.
	 */
	public CanvasElement setAnchor(RectTransform.Anchor anchor) {
		if (rectTransform != null) {
			rectTransform.setAnchor(anchor);
		}
		return this;
	}
	
	/**
	 * Sets the offset of this element on the canvas.
	 * @param x Horizontal offset
	 * @param y Vertical offset
	 */
	public CanvasElement setOffset(int x, int y) {
		if (rectTransform != null) {
			rectTransform.setOffset(x, y);
		}
		return this;
	}
	
	/**
	 * Returns the width of this element.
	 */
	public int getWidth() {
		if (rectTransform == null) {
			return 0;
		}
		
		return rectTransform.width;
	}
	
	/**
	 * Returns the height of this element.
	 */
	public int getHeight() {
		if (rectTransform == null) {
			return 0;
		}
		
		return rectTransform.height;
	}
}
