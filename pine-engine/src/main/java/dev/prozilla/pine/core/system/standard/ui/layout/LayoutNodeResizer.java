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
		int newWidth = 0;
		int newHeight = 0;
		int currentGap = layoutNode.getGap();
		
		if (parentNode.size != null) {
			newWidth = parentNode.size.computeX(parentNode);
			newHeight = parentNode.size.computeY(parentNode);
		}
		
		if (newWidth != 0 && newHeight != 0) {
			newWidth -= parentNode.getPaddingX() * 2;
			newHeight -= parentNode.getPaddingY() * 2;
			
			if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN && !layoutNode.childNodes.isEmpty()) {
				int newGap = layoutNode.direction == Direction.UP || layoutNode.direction == Direction.DOWN ? newHeight : newWidth;
				
				for (Node childNode : layoutNode.childNodes) {
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
				
				if (newGap > currentGap) {
					currentGap = newGap;
				}
			}
		} else if (!layoutNode.childNodes.isEmpty()) {
			int gap = currentGap;
			
			if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN) {
				gap = 0;
			}
			
			int totalChildWidth = 0;
			int totalChildHeight = 0;
			
			for (Node childNode : layoutNode.childNodes) {
				if (childNode.absolutePosition) {
					continue;
				}
				
				totalChildWidth += childNode.currentOuterSize.x;
				totalChildHeight += childNode.currentOuterSize.y;
				
				switch (layoutNode.direction) {
					case UP:
					case DOWN:
						if (childNode.size.x.getUnit() != Unit.PERCENTAGE) {
							newWidth = Math.max(newWidth, childNode.currentOuterSize.x);
						}
						newHeight += childNode.currentOuterSize.y + gap;
						break;
					case LEFT:
					case RIGHT:
						if (childNode.size.y.getUnit() != Unit.PERCENTAGE) {
							newWidth += childNode.currentOuterSize.x + gap;
						}
						newHeight = Math.max(newHeight, childNode.currentOuterSize.y);
						break;
				}
			}
			
			// Subtract gap for last element
			if (layoutNode.direction == Direction.UP || layoutNode.direction == Direction.DOWN) {
				newHeight -= gap;
			} else {
				newWidth -= gap;
			}
			
			if (layoutNode.distribution == LayoutNode.Distribution.SPACE_BETWEEN && parentNode.size != null) {
				int newGap;
				if (layoutNode.direction == Direction.UP || layoutNode.direction == Direction.DOWN) {
					newGap = parentNode.size.computeY(parentNode) - parentNode.getPaddingY() * 2 - totalChildHeight;
				} else {
					newGap = parentNode.size.computeX(parentNode) - parentNode.getPaddingX() * 2 - totalChildWidth;
				}
				
				if (newGap > currentGap) {
					currentGap = newGap;
				}
			}
			
			layoutNode.totalChildrenSize.x = totalChildWidth;
			layoutNode.totalChildrenSize.y = totalChildHeight;
		}
		
		layoutNode.innerSize.x = newWidth;
		layoutNode.innerSize.y = newHeight;
		
		parentNode.currentInnerSize.x = layoutNode.innerSize.x + parentNode.getPaddingX() * 2;
		parentNode.currentInnerSize.y = layoutNode.innerSize.y + parentNode.getPaddingY() * 2;
		
		parentNode.currentOuterSize.x = parentNode.currentInnerSize.x + parentNode.getMarginX() * 2;
		parentNode.currentOuterSize.y = parentNode.currentInnerSize.y + parentNode.getMarginY() * 2;
		
		layoutNode.currentGap = currentGap;
	}
}
