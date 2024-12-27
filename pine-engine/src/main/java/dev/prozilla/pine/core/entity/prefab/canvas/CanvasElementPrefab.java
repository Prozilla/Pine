package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
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
	
	protected DualDimension position;
	protected DualDimension size;
	protected RectTransform.Anchor anchor;
	
	public CanvasElementPrefab() {
		position = new DualDimension();
		size = new DualDimension();
		
		setName("CanvasElement");
	}
	
	/**
	 * Sets the position of this element on the canvas relative to its anchor point.
	 */
	public void setPosition(Dimension x, Dimension y) {
		setPosition(new DualDimension(x, y));
	}
	
	/**
	 * Sets the position of this element on the canvas relative to its anchor point.
	 */
	public void setPosition(DualDimension position) {
		this.position = position;
	}
	
	/**
	 * Sets the size of this element on the canvas.
	 */
	public void setSize(Dimension x, Dimension y) {
		setSize(new DualDimension(x, y));
	}
	
	/**
	 * Sets the size of this element on the canvas.
	 */
	public void setSize(DualDimension size) {
		this.size = size;
	}
	
	/**
	 * Sets the anchor point on the canvas.
	 */
	public void setAnchor(RectTransform.Anchor anchor) {
		this.anchor = anchor;
	}
	
	@Override
	protected void apply(Entity entity) {
		RectTransform rectTransform = entity.addComponent(new RectTransform());
		rectTransform.setPosition(position.clone());
		rectTransform.setSize(size.clone());
		
		if (anchor != null) {
			rectTransform.setAnchor(anchor);
		}
	}
}
