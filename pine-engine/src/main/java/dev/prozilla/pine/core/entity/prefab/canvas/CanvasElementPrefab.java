package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for UI elements.
 */
public class CanvasElementPrefab extends Prefab {
	
	protected RectTransform.Anchor anchor;
	protected int offsetX;
	protected int offsetY;
	
	public CanvasElementPrefab() {
		offsetX = 0;
		offsetY = 0;
		
		setName("CanvasElement");
	}
	
	/**
	 * Sets the position of this element on the canvas.
	 * @param x Horizontal offset
	 * @param y Vertical offset
	 */
	public void setPosition(RectTransform.Anchor anchor, int x, int y) {
		setAnchor(anchor);
		setOffset(x, y);
	}
	
	/**
	 * Sets the anchor point on the canvas.
	 */
	public void setAnchor(RectTransform.Anchor anchor) {
		this.anchor = anchor;
	}
	
	/**
	 * Sets the offset from the starting position of the anchor point on the canvas.
	 * @param x Horizontal offset
	 * @param y Vertical offset
	 */
	public void setOffset(int x, int y) {
		offsetX = x;
		offsetY = y;
	}
	
	@Override
	protected void apply(Entity entity) {
		RectTransform rectTransform = entity.addComponent(new RectTransform());
		rectTransform.setOffset(offsetX, offsetY);
		
		if (anchor != null) {
			rectTransform.setAnchor(anchor);
		}
	}
}
