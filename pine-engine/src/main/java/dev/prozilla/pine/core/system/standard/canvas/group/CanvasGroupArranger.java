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
		int offsetX = containerRect.x + canvasGroup.paddingX;
		int offsetY = containerRect.y + canvasGroup.paddingY;
		switch (canvasGroup.direction) {
			case LEFT -> offsetX = containerRect.x + canvasGroup.innerWidth + canvasGroup.childRects.get(0).width;
			case DOWN -> offsetY = containerRect.y + canvasGroup.innerHeight + canvasGroup.childRects.get(0).height;
		}
		
		// Calculate individual offset for each child rect
		int count = canvasGroup.childRects.size();
		for (int i = 0; i < count; i++) {
			RectTransform childRect = canvasGroup.childRects.get(i);
			
			// Move offset for current child rect
			switch (canvasGroup.direction) {
				case LEFT -> offsetX -= (i == 0)
					? childRect.width * 2 - canvasGroup.paddingX
					: childRect.width + canvasGroup.gap;
				case DOWN -> offsetY -= (i == 0)
					? childRect.height * 2 - canvasGroup.paddingY
					: childRect.height + canvasGroup.gap;
			}
			
			// Set offset for current child rect
			childRect.setPosition(RectTransform.Anchor.BOTTOM_LEFT, offsetX, offsetY);
			
			// Adjust offset based on alignments
			if (canvasGroup.direction == CanvasGroup.Direction.UP || canvasGroup.direction == CanvasGroup.Direction.DOWN) {
				// Vertical alignment
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerWidth - childRect.width));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerWidth - childRect.width) / 2);
				}
			} else if (canvasGroup.direction == CanvasGroup.Direction.LEFT || canvasGroup.direction == CanvasGroup.Direction.RIGHT) {
				// Horizontal alignment
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerHeight - childRect.height));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerHeight - childRect.height) / 2);
				}
			}
			
			// Move offset for next child rect
			switch (canvasGroup.direction) {
				case RIGHT -> offsetX += childRect.width + canvasGroup.gap;
				case UP -> offsetY += childRect.height + canvasGroup.gap;
			}
		}
	}
}
