package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for UI elements that contain other elements.
 */
public class ContainerPrefab extends CanvasElementPrefab {
	
	protected Direction direction;
	protected EdgeAlignment alignment;
	protected Color backgroundColor;
	protected int gap;
	protected DualDimension padding;
	protected boolean arrangeChildren;
	
	public ContainerPrefab() {
		gap = 0;
		padding = new DualDimension();
		backgroundColor = null;
		arrangeChildren = true;
		
		setName("Container");
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setAlignment(EdgeAlignment alignment) {
		this.alignment = alignment;
	}
	
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	
	public void setGap(int gap) {
		this.gap = gap;
	}
	
	public void setPadding(Dimension x, Dimension y) {
		setPadding(new DualDimension(x, y));
	}
	
	public void setPadding(DualDimension padding) {
		this.padding = padding;
	}
	
	public void setArrangeChildren(boolean arrangeChildren) {
		this.arrangeChildren = arrangeChildren;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		CanvasGroup canvasGroup = entity.addComponent(new CanvasGroup());
		
		canvasGroup.gap = gap;
		canvasGroup.backgroundColor = backgroundColor;
		canvasGroup.setPadding(padding.clone());
		canvasGroup.arrangeChildren = arrangeChildren;
		
		if (direction != null) {
			canvasGroup.direction = direction;
		}
		if (alignment != null) {
			canvasGroup.alignment = alignment;
		}
	}
}
