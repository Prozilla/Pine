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
		super(CanvasGroup.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
		RectTransform containerRect = match.getComponent(RectTransform.class);
		
//		match.getEntity().print();
		
		RectMover.anchorRect(containerRect);
		
		if (canvasGroup.childRects.isEmpty()) {
			return;
		}
		
		int offsetX = containerRect.x + canvasGroup.paddingX;
		int offsetY = containerRect.y + canvasGroup.paddingY;
		
		if (canvasGroup.direction == CanvasGroup.Direction.DOWN) {
			offsetY += canvasGroup.innerHeight - canvasGroup.childRects.get(0).height;
		} else if (canvasGroup.direction == CanvasGroup.Direction.LEFT) {
			offsetX += canvasGroup.innerWidth - canvasGroup.childRects.get(0).width;
		}
		
		for (RectTransform childRect : canvasGroup.childRects) {
			childRect.setPosition(RectTransform.Anchor.BOTTOM_LEFT, offsetX, offsetY);
			
			// Calculate alignments
			if (canvasGroup.direction == CanvasGroup.Direction.UP || canvasGroup.direction == CanvasGroup.Direction.DOWN) {
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerWidth - childRect.width));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetX(offsetX + (canvasGroup.innerWidth - childRect.width) / 2);
				}
			} else if (canvasGroup.direction == CanvasGroup.Direction.LEFT || canvasGroup.direction == CanvasGroup.Direction.RIGHT) {
				if (canvasGroup.alignment == CanvasGroup.Alignment.END) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerHeight - childRect.height));
				} else if (canvasGroup.alignment == CanvasGroup.Alignment.CENTER) {
					childRect.setOffsetY(offsetY + (canvasGroup.innerHeight - childRect.height) / 2);
				}
			}
			
			// Move offset for next child rect
			switch (canvasGroup.direction) {
				case RIGHT -> offsetX += childRect.width + canvasGroup.gap;
				case LEFT -> offsetX -= canvasGroup.innerWidth - childRect.width;
				case UP -> offsetY += childRect.height + canvasGroup.gap;
				case DOWN -> offsetY -= childRect.height + canvasGroup.gap;
			}
		}
	}
}
