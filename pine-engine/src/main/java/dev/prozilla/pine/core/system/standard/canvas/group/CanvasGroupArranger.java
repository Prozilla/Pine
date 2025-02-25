package dev.prozilla.pine.core.system.standard.canvas.group;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.standard.canvas.RectUpdater;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Arranges children of canvas groups based on their size and the alignment.
 */
public class CanvasGroupArranger extends UpdateSystem {
	
	public CanvasGroupArranger() {
		super(CanvasGroup.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		CanvasGroup canvasGroup = chunk.getComponent(CanvasGroup.class);
		RectTransform containerRect = chunk.getComponent(RectTransform.class);
		
		RectUpdater.updateRect(containerRect);
		
		if (canvasGroup.childRects.isEmpty() || !canvasGroup.arrangeChildren) {
			return;
		}
		
		// Calculate initial offset
		int offsetX = containerRect.currentPosition.x + canvasGroup.padding.computeX(containerRect);
		int offsetY = containerRect.currentPosition.y + canvasGroup.padding.computeY(containerRect);
		switch (canvasGroup.direction) {
			case LEFT -> offsetX = containerRect.currentPosition.x + canvasGroup.innerSize.x + canvasGroup.childRects.get(0).currentSize.x;
			case DOWN -> offsetY = containerRect.currentPosition.y + canvasGroup.innerSize.y + canvasGroup.childRects.get(0).currentSize.y;
		}
		
		// Calculate individual offset for each child rect
		int count = canvasGroup.childRects.size();
		for (int i = 0; i < count; i++) {
			RectTransform childRect = canvasGroup.childRects.get(i);
			
			// Move offset for current child rect
			switch (canvasGroup.direction) {
				case LEFT -> offsetX -= (i == 0)
					? childRect.currentSize.x * 2 - canvasGroup.padding.computeX(containerRect)
					: childRect.currentSize.x + canvasGroup.gap;
				case DOWN -> offsetY -= (i == 0)
					? childRect.currentSize.y * 2 - canvasGroup.padding.computeY(containerRect)
					: childRect.currentSize.y + canvasGroup.gap;
			}
			
			// Adjust offset based on alignments
			int childOffsetX = offsetX;
			int childOffsetY = offsetY;
			if (canvasGroup.direction == CanvasGroup.Direction.UP || canvasGroup.direction == CanvasGroup.Direction.DOWN) {
				// Vertical alignment
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childOffsetX = offsetX + (canvasGroup.innerSize.x - childRect.currentSize.x);
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childOffsetX = offsetX + (canvasGroup.innerSize.x - childRect.currentSize.x) / 2;
				}
			} else if (canvasGroup.direction == CanvasGroup.Direction.LEFT || canvasGroup.direction == CanvasGroup.Direction.RIGHT) {
				// Horizontal alignment
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childOffsetY = offsetY + (canvasGroup.innerSize.y - childRect.currentSize.y);
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childOffsetY = offsetY + (canvasGroup.innerSize.y - childRect.currentSize.y) / 2;
				}
			}
			
			// Set offset for current child rect
			childRect.setAnchor(RectTransform.Anchor.BOTTOM_LEFT);
			childRect.position.set(childOffsetX, childOffsetY);
			
			// Move offset for next child rect
			switch (canvasGroup.direction) {
				case RIGHT -> offsetX += childRect.currentSize.x + canvasGroup.gap;
				case UP -> offsetY += childRect.currentSize.y + canvasGroup.gap;
			}
		}
	}
}
