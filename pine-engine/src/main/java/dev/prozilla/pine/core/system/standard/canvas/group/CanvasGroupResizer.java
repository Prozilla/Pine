package dev.prozilla.pine.core.system.standard.canvas.group;

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
		
		if (!canvasGroup.childRects.isEmpty()) {
			for (RectTransform childRect : canvasGroup.childRects) {
				switch (canvasGroup.direction) {
					case UP:
					case DOWN:
						newWidth = Math.max(newWidth, childRect.size.x);
						newHeight += childRect.size.y + canvasGroup.gap;
						break;
					case LEFT:
					case RIGHT:
						newWidth += childRect.size.x + canvasGroup.gap;
						newHeight = Math.max(newHeight, childRect.size.y);
						break;
				}
			}
			
			// Subtract gap for last element
			if (canvasGroup.direction == CanvasGroup.Direction.UP || canvasGroup.direction == CanvasGroup.Direction.DOWN) {
				newHeight -= canvasGroup.gap;
			} else {
				newWidth -= canvasGroup.gap;
			}
		}
		
		canvasGroup.innerSize.x = newWidth;
		canvasGroup.innerSize.y = newHeight;
		
		rect.size.x = canvasGroup.innerSize.x + canvasGroup.padding.x * 2;
		rect.size.y = canvasGroup.innerSize.y + canvasGroup.padding.y * 2;
	}
}
