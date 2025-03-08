package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.TooltipRenderer;
import dev.prozilla.pine.core.entity.Entity;

public class TooltipPrefab extends ContainerPrefab {
	
	protected DualDimension offset;
	
	public TooltipPrefab() {
		setBackgroundColor(Color.black());
		setDirection(Direction.DOWN);
		setAbsolutePosition(true);
		setPassThrough(true);
		setName("Tooltip");
		
		offset = new DualDimension();
	}
	
	public void setOffsetX(DimensionBase offsetX) {
		setOffset(new DualDimension(offsetX, Dimension.auto()));
	}
	
	public void setOffsetY(DimensionBase offsetY) {
		setOffset(new DualDimension(Dimension.auto(), offsetY));
	}
	
	public void setOffset(DualDimension offset) {
		this.offset = offset;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new TooltipRenderer(offset));
	}
}
