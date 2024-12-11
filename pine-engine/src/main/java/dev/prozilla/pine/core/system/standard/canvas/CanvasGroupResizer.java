package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Resizes canvas groups based on the sizes of their children.
 */
public class CanvasGroupResizer extends UpdateSystem {
	
	public CanvasGroupResizer() {
		super(CanvasGroup.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		int newWidth = 0;
		int newHeight = 0;
		
		if (!canvasGroup.childRects.isEmpty()) {
			for (RectTransform childRect : canvasGroup.childRects) {
				switch (canvasGroup.direction) {
					case UP:
					case DOWN:
						newWidth = Math.max(newWidth, childRect.width);
						newHeight += childRect.height + canvasGroup.gap;
						break;
					case LEFT:
					case RIGHT:
						newWidth += childRect.width + canvasGroup.gap;
						newHeight = Math.max(newHeight, childRect.height);
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
		
		canvasGroup.innerWidth = newWidth;
		canvasGroup.innerHeight = newHeight;
		
		rect.width = canvasGroup.innerWidth + canvasGroup.paddingX * 2;
		rect.height = canvasGroup.innerHeight + canvasGroup.paddingY * 2;
	}
}
