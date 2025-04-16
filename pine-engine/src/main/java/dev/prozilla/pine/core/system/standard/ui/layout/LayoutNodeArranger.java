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
	
	public static void arrangeLayoutNode(LayoutNode layoutNode, Node containerRect) {
		NodeUpdater.updateNode(containerRect);
		
		if (layoutNode.childNodes.isEmpty() || !layoutNode.arrangeChildren) {
			return;
		}
		
		int gap = layoutNode.getGap();
		
		// Calculate initial offset
		int offsetX = containerRect.currentPosition.x + containerRect.getPaddingX();
		int offsetY = containerRect.currentPosition.y + containerRect.getPaddingY();
		
		switch (layoutNode.direction) {
			case LEFT -> offsetX = containerRect.currentPosition.x + layoutNode.innerSize.x + layoutNode.childNodes.getFirst().currentSize.x;
			case DOWN -> offsetY = containerRect.currentPosition.y + layoutNode.innerSize.y + layoutNode.childNodes.getFirst().currentSize.y;
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
					? childNode.currentSize.x * 2 - containerRect.getPaddingX()
					: childNode.currentSize.x + gap;
				case DOWN -> offsetY -= (i == 0)
					? childNode.currentSize.y * 2 - containerRect.getPaddingY()
					: childNode.currentSize.y + gap;
			}
			
			// Adjust offset based on alignments
			int childOffsetX = offsetX;
			int childOffsetY = offsetY;
			if (layoutNode.direction == Direction.UP || layoutNode.direction == Direction.DOWN) {
				// Vertical alignment
				if (layoutNode.alignment == EdgeAlignment.END) {
					childOffsetX = offsetX + (layoutNode.innerSize.x - childNode.currentSize.x);
				} else if (layoutNode.alignment == EdgeAlignment.CENTER) {
					childOffsetX = offsetX + (layoutNode.innerSize.x - childNode.currentSize.x) / 2;
				}
			} else if (layoutNode.direction == Direction.LEFT || layoutNode.direction == Direction.RIGHT) {
				// Horizontal alignment
				if (layoutNode.alignment == EdgeAlignment.END) {
					childOffsetY = offsetY + (layoutNode.innerSize.y - childNode.currentSize.y);
				} else if (layoutNode.alignment == EdgeAlignment.CENTER) {
					childOffsetY = offsetY + (layoutNode.innerSize.y - childNode.currentSize.y) / 2;
				}
			}
			
			// Set offset for current child node
			childNode.anchor = GridAlignment.BOTTOM_LEFT;
			childNode.position.set(childOffsetX, childOffsetY);
			childNode.iterations++;
			
			if (childNode.iterations > 4) {
				childNode.readyToRender = true;
			}
			
			// Move offset for next child node
			switch (layoutNode.direction) {
				case RIGHT -> offsetX += childNode.currentSize.x + gap;
				case UP -> offsetY += childNode.currentSize.y + gap;
			}
		}
	}
}
