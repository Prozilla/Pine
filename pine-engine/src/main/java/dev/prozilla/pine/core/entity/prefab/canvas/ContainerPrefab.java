package dev.prozilla.pine.core.entity.prefab.canvas;

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
	protected int paddingX;
	protected int paddingY;
	
	public ContainerPrefab() {
		gap = 0;
		paddingX = 0;
		paddingY = 0;
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
	
	public void setPadding(int x, int y) {
		paddingX = x;
		paddingY = y;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		CanvasGroup canvasGroup = entity.addComponent(new CanvasGroup());
		
		canvasGroup.gap = gap;
		canvasGroup.padding.x = paddingX;
		canvasGroup.padding.y = paddingY;
		canvasGroup.backgroundColor = backgroundColor;
		
		if (direction != null) {
			canvasGroup.direction = direction;
		}
		if (alignment != null) {
			canvasGroup.alignment = alignment;
		}
	}
}
