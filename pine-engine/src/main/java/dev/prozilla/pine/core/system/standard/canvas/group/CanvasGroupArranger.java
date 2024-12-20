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
		
		if (canvasGroup.childRects.isEmpty()) {
			return;
		}
		
		// Calculate initial offset
		int offsetX = containerRect.position.x + canvasGroup.padding.x;
		int offsetY = containerRect.position.y + canvasGroup.padding.y;
		switch (canvasGroup.direction) {
			case LEFT -> offsetX = containerRect.position.x + canvasGroup.innerSize.x + canvasGroup.childRects.get(0).size.x;
			case DOWN -> offsetY = containerRect.position.y + canvasGroup.innerSize.y + canvasGroup.childRects.get(0).size.y;
		}
		
		// Calculate individual offset for each child rect
		int count = canvasGroup.childRects.size();
		for (int i = 0; i < count; i++) {
			RectTransform childRect = canvasGroup.childRects.get(i);
			
			// Move offset for current child rect
			switch (canvasGroup.direction) {
				case LEFT -> offsetX -= (i == 0)
					? childRect.size.x * 2 - canvasGroup.padding.x
					: childRect.size.x + canvasGroup.gap;
				case DOWN -> offsetY -= (i == 0)
					? childRect.size.y * 2 - canvasGroup.padding.y
					: childRect.size.y + canvasGroup.gap;
			}
			
			// Set offset for current child rect
			childRect.setPosition(RectTransform.Anchor.BOTTOM_LEFT, offsetX, offsetY);
			
			// Adjust offset based on alignments
			if (canvasGroup.direction == CanvasGroup.Direction.UP || canvasGroup.direction == CanvasGroup.Direction.DOWN) {
				// Vertical alignment
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerSize.x - childRect.size.x));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerSize.x - childRect.size.x) / 2);
				}
			} else if (canvasGroup.direction == CanvasGroup.Direction.LEFT || canvasGroup.direction == CanvasGroup.Direction.RIGHT) {
				// Horizontal alignment
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerSize.y - childRect.size.y));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerSize.y - childRect.size.y) / 2);
				}
			}
			
			// Move offset for next child rect
			switch (canvasGroup.direction) {
				case RIGHT -> offsetX += childRect.size.x + canvasGroup.gap;
				case UP -> offsetY += childRect.size.y + canvasGroup.gap;
			}
		}
	}
}
