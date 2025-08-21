package dev.prozilla.pine.core.system.standard.ui.layout;

import dev.prozilla.pine.common.math.dimension.Unit;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Resizes layout nodes based on the sizes of their children.
 */
public class LayoutNodeResizer extends UpdateSystem {
	
	public LayoutNodeResizer() {
		super(LayoutNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		LayoutNode layoutNode = chunk.getComponent(LayoutNode.class);
		Node node = chunk.getComponent(Node.class);
		resizeCanvasGroup(layoutNode, node);
	}
	
	public static void resizeCanvasGroup(LayoutNode layoutNode, Node parentNode) {
		// New inner size of the node without padding
		float innerWidth = 0, innerHeight = 0;
		float currentGap = layoutNode.getGap();
		
//		if (parentNode.getEntity().hasTag("BuildMenu")) {
//			Logger.system.log("Size: " + parentNode.size.compute(parentNode));
//		}
		
		// Check if the node has a defined size;
		if (parentNode.size != null) {
			innerWidth = parentNode.size.computeX(parentNode);
			innerHeight = parentNode.size.computeY(parentNode);
		}
		
		// Calculate total outer size of all children
		layoutNode.totalChildrenSize.set(0);
		for (Node childNode : layoutNode.childNodes) {
			if (!childNode.absolutePosition) {
				layoutNode.totalChildrenSize.add(childNode.currentOuterSize);
			}
		}
		
		// Calculate width
		if (innerWidth != 0) {
			// Subtract padding to get inner size
			innerWidth -= parentNode.getPaddingX() * 2;
			
			// Logic for space between distribution
			if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN && !layoutNode.childNodes.isEmpty()) {
				float newGap = calculateSpaceBetweenGap(layoutNode, innerHeight, innerWidth);
				currentGap = Math.max(newGap, currentGap);
			}
		} else if (!layoutNode.childNodes.isEmpty()) {
			float gap = currentGap;
			
			if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN) {
				gap = 0;
			}
			
			for (Node childNode : layoutNode.childNodes) {
				if (childNode.absolutePosition) {
					continue;
				}
				
				switch (layoutNode.direction) {
					case UP:
					case DOWN:
						if (childNode.size.x.getUnit() != Unit.PERCENTAGE) {
							innerWidth = Math.max(innerWidth, childNode.currentOuterSize.x);
						}
						break;
					case LEFT:
					case RIGHT:
						if (childNode.size.y.getUnit() != Unit.PERCENTAGE) {
							innerWidth += childNode.currentOuterSize.x + gap;
						}
						break;
				}
			}
			
			// Subtract gap for last element
			if (layoutNode.direction.isHorizontal()) {
				innerWidth -= gap;
			}
			
			// Logic for space between distribution
			currentGap = calculateSpaceBetweenGap2(layoutNode, parentNode, currentGap);
		}
		
		// Calculate height
		if (innerHeight != 0) {
			// Subtract padding to get inner size
			innerHeight -= parentNode.getPaddingY() * 2;
			
			// Logic for space between distribution
			if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN && !layoutNode.childNodes.isEmpty()) {
				float newGap = calculateSpaceBetweenGap(layoutNode, innerHeight, innerWidth);
				currentGap = Math.max(newGap, currentGap);
			}
		} else if (!layoutNode.childNodes.isEmpty()) {
			float gap = currentGap;
			
			if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN) {
				gap = 0;
			}
			
			for (Node childNode : layoutNode.childNodes) {
				if (childNode.absolutePosition) {
					continue;
				}
				
				switch (layoutNode.direction) {
					case UP:
					case DOWN:
						innerHeight += childNode.currentOuterSize.y + gap;
						break;
					case LEFT:
					case RIGHT:
						innerHeight = Math.max(innerHeight, childNode.currentOuterSize.y);
						break;
				}
			}
			
			// Subtract gap for last element
			if (layoutNode.direction.isVertical()) {
				innerHeight -= gap;
			}
			
			// Logic for space between distribution
			currentGap = calculateSpaceBetweenGap2(layoutNode, parentNode, currentGap);
		}
		
		// Content size of the node (without padding)
		layoutNode.innerSize.x = innerWidth;
		layoutNode.innerSize.y = innerHeight;
		
		// Inner size of the node (with padding)
		parentNode.currentInnerSize.x = layoutNode.innerSize.x + parentNode.getPaddingX() * 2;
		parentNode.currentInnerSize.y = layoutNode.innerSize.y + parentNode.getPaddingY() * 2;
		
		// Outer size of the node (with margin)
		parentNode.currentOuterSize.x = parentNode.currentInnerSize.x + parentNode.getMarginX() * 2;
		parentNode.currentOuterSize.y = parentNode.currentInnerSize.y + parentNode.getMarginY() * 2;
		
		layoutNode.currentGap = currentGap;
		
//		if (parentNode.getEntity().hasTag("BuildMenu")) {
//			Logger.system.log("Inner size: " + parentNode.currentInnerSize);
//			Logger.system.log("Outer size: " + parentNode.currentOuterSize);
//		}
	}
	
	private static float calculateSpaceBetweenGap2(LayoutNode layoutNode, Node parentNode, float currentGap) {
		if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN && parentNode.size != null) {
			float newGap;
			if (layoutNode.direction == Direction.UP || layoutNode.direction == Direction.DOWN) {
				newGap = parentNode.size.computeY(parentNode) - parentNode.getPaddingY() * 2 - layoutNode.totalChildrenSize.x;
			} else {
				newGap = parentNode.size.computeX(parentNode) - parentNode.getPaddingX() * 2 - layoutNode.totalChildrenSize.y;
			}
			
			if (newGap > currentGap) {
				currentGap = newGap;
			}
		}
		return currentGap;
	}
	
	private static float calculateSpaceBetweenGap(LayoutNode layoutNode, float innerHeight, float innerWidth) {
		float newGap = layoutNode.direction.isVertical() ? innerHeight : innerWidth;
		
		// Subtract outer sizes of children from gap
		for (Node childNode : layoutNode.childNodes) {
			if (!childNode.absolutePosition) {
				switch (layoutNode.direction) {
					case UP:
					case DOWN:
						newGap -= childNode.currentOuterSize.y;
						break;
					case LEFT:
					case RIGHT:
						newGap -= childNode.currentOuterSize.x;
						break;
				}
			}
		}
		return newGap;
	}
}
