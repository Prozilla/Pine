package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Arranges children of canvas groups based on their size and the alignment.
 */
public class CanvasGroupArranger extends UpdateSystem {
	
	public CanvasGroupArranger() {
		super(CanvasGroup.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
		
		if (canvasGroup.childRects.isEmpty()) {
			return;
		}
		
		int offsetX = canvasGroup.x + canvasGroup.paddingX;
		int offsetY = canvasGroup.y + canvasGroup.paddingY;
		
		if (canvasGroup.direction == CanvasGroup.Direction.DOWN) {
			offsetY += canvasGroup.innerHeight - canvasGroup.childRects.get(0).getHeight();
		} else if (canvasGroup.direction == CanvasGroup.Direction.LEFT) {
			offsetX += canvasGroup.innerWidth - canvasGroup.childRects.get(0).getWidth();
		}
		
		for (RectTransform childRect : canvasGroup.childRects) {
			childRect.setPosition(RectTransform.Anchor.BOTTOM_LEFT, offsetX, offsetY);
			
			// Calculate alignments
			if (canvasGroup.direction == CanvasGroup.Direction.UP || canvasGroup.direction == CanvasGroup.Direction.DOWN) {
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerWidth - childRect.getWidth()));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerWidth - childRect.getWidth()) / 2);
				}
			} else if (canvasGroup.direction == CanvasGroup.Direction.LEFT || canvasGroup.direction == CanvasGroup.Direction.RIGHT) {
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerHeight - childRect.getHeight()));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerHeight - childRect.getHeight()) / 2);
				}
			}
			
			// Move offset for next child rect
			switch (canvasGroup.direction) {
				case RIGHT:
					offsetX += childRect.getWidth() + canvasGroup.gap;
					break;
				case LEFT:
					offsetX -= canvasGroup.innerWidth - childRect.getWidth();
					break;
				case UP:
					offsetY += childRect.getHeight() + canvasGroup.gap;
					break;
				case DOWN:
					offsetY -= childRect.getHeight() + canvasGroup.gap;
					break;
			}
		}
	}
}
