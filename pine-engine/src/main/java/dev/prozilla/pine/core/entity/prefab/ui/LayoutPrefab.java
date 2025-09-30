package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.style.LayoutNodeStyle;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for UI layouts.
 */
@Components({ LayoutNode.class, Node.class, Transform.class })
public class LayoutPrefab extends NodePrefab {
	
	protected Direction direction;
	protected EdgeAlignment alignment;
	protected LayoutNode.Distribution distribution;
	protected DimensionBase gap;
	protected boolean arrangeChildren;
	
	public LayoutPrefab() {
		arrangeChildren = true;
		
		setName("Container");
		addClass("container");
		setHTMLTag("div");
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setAlignment(EdgeAlignment alignment) {
		this.alignment = alignment;
	}
	
	public void setGap(DimensionBase gap) {
		this.gap = gap;
	}
	
	public void setArrangeChildren(boolean arrangeChildren) {
		this.arrangeChildren = arrangeChildren;
	}
	
	public void setDistribution(LayoutNode.Distribution distribution) {
		this.distribution = distribution;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		LayoutNode layoutNode = entity.addComponent(new LayoutNode());
		
		layoutNode.gap = gap;
		layoutNode.arrangeChildren = arrangeChildren;
		
		if (direction != null) {
			layoutNode.direction = direction;
		}
		if (alignment != null) {
			layoutNode.alignment = alignment;
		}
		if (distribution != null) {
			layoutNode.distribution = distribution;
		}
		
		if (styleSheet != null) {
			Node node = entity.getComponent(Node.class);
			AnimationData animationData = entity.getComponent(AnimationData.class);
			entity.addComponent(new LayoutNodeStyle(animationData, node, styleSheet));
		}
	}
}
