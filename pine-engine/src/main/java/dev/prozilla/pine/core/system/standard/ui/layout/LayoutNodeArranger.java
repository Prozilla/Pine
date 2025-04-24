package dev.prozilla.pine.core.system.standard.ui.layout;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.standard.ui.NodeUpdater;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Arranges children of layout nodes based on their size and the alignment and distribution of the layout.
 */
public class LayoutNodeArranger extends UpdateSystem {
	
	public LayoutNodeArranger() {
		super(LayoutNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		LayoutNode layoutNode = chunk.getComponent(LayoutNode.class);
		Node node = chunk.getComponent(Node.class);
		arrangeLayoutNode(layoutNode, node);
	}
	
	public static void arrangeLayoutNode(LayoutNode layoutNode, Node parentNode) {
		NodeUpdater.updateNode(parentNode);
		
		if (layoutNode.childNodes.isEmpty() || !layoutNode.arrangeChildren) {
			return;
		}
		
		float gap = layoutNode.getGap();
		
		// Calculate initial offset
		float offsetX = parentNode.currentPosition.x + parentNode.getPaddingX();
		float offsetY = parentNode.currentPosition.y + parentNode.getPaddingY();
		
		switch (layoutNode.direction) {
			case LEFT -> offsetX = parentNode.currentPosition.x + layoutNode.innerSize.x + layoutNode.childNodes.getFirst().currentOuterSize.x;
			case DOWN -> offsetY = parentNode.currentPosition.y + layoutNode.innerSize.y + layoutNode.childNodes.getFirst().currentOuterSize.y;
		}
		
		if (layoutNode.distribution == LayoutNode.Distribution.CENTER) {
			switch (layoutNode.direction) {
				case LEFT, RIGHT -> offsetX += (layoutNode.innerSize.x - (layoutNode.totalChildrenSize.x + gap * (layoutNode.childNodes.size() - 1))) / 2;
				case UP, DOWN -> offsetY += (layoutNode.innerSize.y - (layoutNode.totalChildrenSize.y + gap * (layoutNode.childNodes.size() - 1))) / 2;
			}
		}
		
		// Calculate individual offset for each child node
		int count = layoutNode.childNodes.size();
		for (int i = 0; i < count; i++) {
			Node childNode = layoutNode.childNodes.get(i);
			
			if (childNode.absolutePosition) {
				continue;
			}
			
			// Move offset for current child node
			switch (layoutNode.direction) {
				case LEFT -> offsetX -= (i == 0)
					? childNode.currentOuterSize.x * 2 - parentNode.getPaddingX()
					: childNode.currentOuterSize.x + gap;
				case DOWN -> offsetY -= (i == 0)
					? childNode.currentOuterSize.y * 2 - parentNode.getPaddingY()
					: childNode.currentOuterSize.y + gap;
			}
			
			// Adjust offset based on alignments
			float childOffsetX = offsetX;
			float childOffsetY = offsetY;
			if (layoutNode.direction == Direction.UP || layoutNode.direction == Direction.DOWN) {
				// Vertical alignment
				if (layoutNode.alignment == EdgeAlignment.END) {
					childOffsetX = offsetX + (layoutNode.innerSize.x - childNode.currentOuterSize.x);
				} else if (layoutNode.alignment == EdgeAlignment.CENTER) {
					childOffsetX = offsetX + (layoutNode.innerSize.x - childNode.currentOuterSize.x) / 2;
				}
			} else if (layoutNode.direction == Direction.LEFT || layoutNode.direction == Direction.RIGHT) {
				// Horizontal alignment
				if (layoutNode.alignment == EdgeAlignment.END) {
					childOffsetY = offsetY + (layoutNode.innerSize.y - childNode.currentOuterSize.y);
				} else if (layoutNode.alignment == EdgeAlignment.CENTER) {
					childOffsetY = offsetY + (layoutNode.innerSize.y - childNode.currentOuterSize.y) / 2;
				}
			}
			
			// Set offset for current child node
			childNode.anchor = GridAlignment.BOTTOM_LEFT;
			childNode.offset.x = childOffsetX;
			childNode.offset.y = childOffsetY;
			childNode.iterations++;
			
			if (childNode.iterations > 4) {
				childNode.readyToRender = true;
			}
			
			// Move offset for next child node
			switch (layoutNode.direction) {
				case RIGHT -> offsetX += childNode.currentOuterSize.x + gap;
				case UP -> offsetY += childNode.currentOuterSize.y + gap;
			}
		}
	}
}
