package dev.prozilla.pine.core.system.standard.canvas.group;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Resizes canvas groups based on the sizes of their children.
 */
public class CanvasGroupResizer extends UpdateSystem {
	
	public CanvasGroupResizer() {
		super(CanvasGroup.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		CanvasGroup canvasGroup = chunk.getComponent(CanvasGroup.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		int newWidth = 0;
		int newHeight = 0;
		
		if (rect.size != null) {
			newWidth = rect.size.computeX(rect);
			newHeight = rect.size.computeY(rect);
		}
			
		if (newWidth != 0 && newHeight != 0) {
			newWidth -= canvasGroup.padding.computeX(rect) * 2;
			newHeight -= canvasGroup.padding.computeY(rect) * 2;
		} else if (!canvasGroup.childRects.isEmpty()) {
			for (RectTransform childRect : canvasGroup.childRects) {
				switch (canvasGroup.direction) {
					case UP:
					case DOWN:
						newWidth = Math.max(newWidth, childRect.currentSize.x);
						newHeight += childRect.currentSize.y + canvasGroup.gap;
						break;
					case LEFT:
					case RIGHT:
						newWidth += childRect.currentSize.x + canvasGroup.gap;
						newHeight = Math.max(newHeight, childRect.currentSize.y);
						break;
				}
			}
			
			// Subtract gap for last element
			if (canvasGroup.direction == Direction.UP || canvasGroup.direction == Direction.DOWN) {
				newHeight -= canvasGroup.gap;
			} else {
				newWidth -= canvasGroup.gap;
			}
		}
		
		canvasGroup.innerSize.x = newWidth;
		canvasGroup.innerSize.y = newHeight;
		
		rect.currentSize.x = canvasGroup.innerSize.x + canvasGroup.padding.computeX(rect) * 2;
		rect.currentSize.y = canvasGroup.innerSize.y + canvasGroup.padding.computeY(rect) * 2;
	}
}
