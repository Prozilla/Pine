package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for UI elements that contain other elements.
 */
public class ContainerPrefab extends CanvasElementPrefab {
	
	protected Direction direction;
	protected EdgeAlignment alignment;
	protected CanvasGroup.Distribution distribution;
	protected int gap;
	protected boolean arrangeChildren;
	
	public ContainerPrefab() {
		gap = 0;
		arrangeChildren = true;
		
		setName("Container");
		addClass("container");
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setAlignment(EdgeAlignment alignment) {
		this.alignment = alignment;
	}
	
	public void setGap(int gap) {
		this.gap = gap;
	}
	
	public void setArrangeChildren(boolean arrangeChildren) {
		this.arrangeChildren = arrangeChildren;
	}
	
	public void setDistribution(CanvasGroup.Distribution distribution) {
		this.distribution = distribution;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		CanvasGroup canvasGroup = entity.addComponent(new CanvasGroup());
		
		canvasGroup.gap = gap;
		canvasGroup.arrangeChildren = arrangeChildren;
		
		if (direction != null) {
			canvasGroup.direction = direction;
		}
		if (alignment != null) {
			canvasGroup.alignment = alignment;
		}
		if (distribution != null) {
			canvasGroup.distribution = distribution;
		}
	}
}
