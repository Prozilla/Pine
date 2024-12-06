package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.system.UpdateSystem;

public class CanvasGroupResizer extends UpdateSystem {
	
	public CanvasGroupResizer() {
		super(CanvasGroup.class);
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
			CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
			
			int newWidth = 0;
			int newHeight = 0;
			
			if (!canvasGroup.childRects.isEmpty()) {
				for (RectTransform childRect : canvasGroup.childRects) {
					switch (canvasGroup.direction) {
						case UP:
						case DOWN:
							newWidth = Math.max(newWidth, childRect.getWidth());
							newHeight += childRect.getHeight() + canvasGroup.gap;
							break;
						case LEFT:
						case RIGHT:
							newWidth += childRect.getWidth() + canvasGroup.gap;
							newHeight = Math.max(newHeight, childRect.getHeight());
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
			
			canvasGroup.width = canvasGroup.innerWidth + canvasGroup.paddingX * 2;
			canvasGroup.height = canvasGroup.innerHeight + canvasGroup.paddingY * 2;
		});
	}
}
