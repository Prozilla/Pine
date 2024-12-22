package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for UI elements.
 */
@Components({ RectTransform.class, Transform.class })
public class CanvasElementPrefab extends Prefab {
	
	protected RectTransform.Anchor anchor;
	protected int offsetX;
	protected int offsetY;
	protected boolean fillContainerWidth;
	protected boolean fillContainerHeight;
	
	public CanvasElementPrefab() {
		offsetX = 0;
		offsetY = 0;
		fillContainerWidth = false;
		fillContainerHeight = false;
		
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
	
	/**
	 * Sets the value that determines whether the rect should fill its container.
	 */
	public void setFillContainer(boolean fillContainer) {
		setFillContainerWidth(fillContainer);
		setFillContainerHeight(fillContainer);
	}
	
	public void setFillContainerWidth(boolean fillContainerWidth) {
		this.fillContainerWidth = fillContainerWidth;
	}
	
	public void setFillContainerHeight(boolean fillContainerHeight) {
		this.fillContainerHeight = fillContainerHeight;
	}
	
	@Override
	protected void apply(Entity entity) {
		RectTransform rectTransform = entity.addComponent(new RectTransform());
		rectTransform.setOffset(offsetX, offsetY);
		rectTransform.fillContainerWidth = fillContainerWidth;
		rectTransform.fillContainerHeight = fillContainerHeight;
		
		if (anchor != null) {
			rectTransform.setAnchor(anchor);
		}
	}
}
