package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for UI elements that contain other elements.
 */
public class ContainerPrefab extends CanvasElementPrefab {
	
	protected CanvasGroup.Direction direction;
	protected CanvasGroup.Alignment alignment;
	protected Color backgroundColor;
	protected int gap;
	protected DualDimension padding;
	
	public ContainerPrefab() {
		gap = 0;
		padding = new DualDimension();
		backgroundColor = null;
		
		setName("Container");
	}
	
	public void setDirection(CanvasGroup.Direction direction) {
		this.direction = direction;
	}
	
	public void setAlignment(CanvasGroup.Alignment alignment) {
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
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		CanvasGroup canvasGroup = entity.addComponent(new CanvasGroup());
		
		canvasGroup.gap = gap;
		canvasGroup.backgroundColor = backgroundColor;
		canvasGroup.setPadding(padding.clone());
		
		if (direction != null) {
			canvasGroup.direction = direction;
		}
		if (alignment != null) {
			canvasGroup.alignment = alignment;
		}
	}
}
