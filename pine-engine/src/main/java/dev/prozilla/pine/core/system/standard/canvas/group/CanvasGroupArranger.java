package dev.prozilla.pine.core.system.standard.canvas.group;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
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
		RectTransform rect = chunk.getComponent(RectTransform.class);
		arrangeCanvasGroup(canvasGroup, rect);
	}
	
	public static void arrangeCanvasGroup(CanvasGroup canvasGroup, RectTransform containerRect) {
		RectUpdater.updateRect(containerRect);
		
		if (canvasGroup.childRects.isEmpty() || !canvasGroup.arrangeChildren) {
			return;
		}
		
		// Calculate initial offset
		int offsetX = containerRect.currentPosition.x + containerRect.getPaddingX();
		int offsetY = containerRect.currentPosition.y + containerRect.getPaddingY();
		
		switch (canvasGroup.direction) {
			case LEFT -> offsetX = containerRect.currentPosition.x + canvasGroup.innerSize.x + canvasGroup.childRects.getFirst().currentSize.x;
			case DOWN -> offsetY = containerRect.currentPosition.y + canvasGroup.innerSize.y + canvasGroup.childRects.getFirst().currentSize.y;
		}
		
		if (canvasGroup.distribution == CanvasGroup.Distribution.CENTER) {
			switch (canvasGroup.direction) {
				case LEFT, RIGHT -> offsetX += (canvasGroup.innerSize.x - (canvasGroup.totalChildrenSize.x + canvasGroup.gap * (canvasGroup.childRects.size() - 1))) / 2;
				case UP, DOWN -> offsetY += (canvasGroup.innerSize.y - (canvasGroup.totalChildrenSize.y + canvasGroup.gap * (canvasGroup.childRects.size() - 1))) / 2;
			}
		}
		
		// Calculate individual offset for each child rect
		int count = canvasGroup.childRects.size();
		for (int i = 0; i < count; i++) {
			RectTransform childRect = canvasGroup.childRects.get(i);
			
			if (childRect.absolutePosition) {
				continue;
			}
			
			// Move offset for current child rect
			switch (canvasGroup.direction) {
				case LEFT -> offsetX -= (i == 0)
					? childRect.currentSize.x * 2 - containerRect.getPaddingX()
					: childRect.currentSize.x + canvasGroup.gap;
				case DOWN -> offsetY -= (i == 0)
					? childRect.currentSize.y * 2 - containerRect.getPaddingY()
					: childRect.currentSize.y + canvasGroup.gap;
			}
			
			// Adjust offset based on alignments
			int childOffsetX = offsetX;
			int childOffsetY = offsetY;
			if (canvasGroup.direction == Direction.UP || canvasGroup.direction == Direction.DOWN) {
				// Vertical alignment
				if (canvasGroup.alignment == EdgeAlignment.END) {
					childOffsetX = offsetX + (canvasGroup.innerSize.x - childRect.currentSize.x);
				} else if (canvasGroup.alignment == EdgeAlignment.CENTER) {
					childOffsetX = offsetX + (canvasGroup.innerSize.x - childRect.currentSize.x) / 2;
				}
			} else if (canvasGroup.direction == Direction.LEFT || canvasGroup.direction == Direction.RIGHT) {
				// Horizontal alignment
				if (canvasGroup.alignment == EdgeAlignment.END) {
					childOffsetY = offsetY + (canvasGroup.innerSize.y - childRect.currentSize.y);
				} else if (canvasGroup.alignment == EdgeAlignment.CENTER) {
					childOffsetY = offsetY + (canvasGroup.innerSize.y - childRect.currentSize.y) / 2;
				}
			}
			
			// Set offset for current child rect
			childRect.anchor = GridAlignment.BOTTOM_LEFT;
			childRect.position.set(childOffsetX, childOffsetY);
			childRect.iterations++;
			
			if (childRect.iterations > 4) {
				childRect.readyToRender = true;
			}
			
			// Move offset for next child rect
			switch (canvasGroup.direction) {
				case RIGHT -> offsetX += childRect.currentSize.x + canvasGroup.gap;
				case UP -> offsetY += childRect.currentSize.y + canvasGroup.gap;
			}
		}
	}
}
