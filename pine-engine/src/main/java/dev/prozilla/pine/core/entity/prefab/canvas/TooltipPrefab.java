package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.TooltipRenderer;
import dev.prozilla.pine.core.entity.Entity;

public class TooltipPrefab extends ContainerPrefab {
	
	public TooltipPrefab() {
		setBackgroundColor(Color.black());
		setDirection(Direction.DOWN);
		setAbsolutePosition(true);
		setPassThrough(true);
		setName("Tooltip");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new TooltipRenderer());
	}
}
