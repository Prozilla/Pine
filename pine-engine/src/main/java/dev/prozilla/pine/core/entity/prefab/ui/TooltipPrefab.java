package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TooltipNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for tooltips in the UI.
 */
@Components({ TooltipNode.class, LayoutNode.class, Node.class, Transform.class })
public class TooltipPrefab extends LayoutPrefab {
	
	protected DualDimension offset;
	
	public TooltipPrefab() {
		setBackgroundColor(Color.black());
		setDirection(Direction.DOWN);
		setAbsolutePosition(true);
		setPassThrough(true);
		setName("Tooltip");
		addClass("tooltip");
		
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
		
		entity.addComponent(new TooltipNode(offset));
	}
}
