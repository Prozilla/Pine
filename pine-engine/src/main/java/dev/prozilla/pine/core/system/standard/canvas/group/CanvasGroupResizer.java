package dev.prozilla.pine.core.system.standard.canvas.group;

import dev.prozilla.pine.common.math.dimension.Unit;
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
		resizeCanvasGroup(canvasGroup, rect);
	}
	
	public static void resizeCanvasGroup(CanvasGroup canvasGroup, RectTransform containerRect) {
		int newWidth = 0;
		int newHeight = 0;
		
		if (containerRect.size != null) {
			newWidth = containerRect.size.computeX(containerRect);
			newHeight = containerRect.size.computeY(containerRect);
		}
		
		if (newWidth != 0 && newHeight != 0) {
			newWidth -= canvasGroup.padding.computeX(containerRect) * 2;
			newHeight -= canvasGroup.padding.computeY(containerRect) * 2;
			
			if (canvasGroup.distribution == CanvasGroup.Distribution.SPACE_BETWEEN && !canvasGroup.childRects.isEmpty()) {
				canvasGroup.gap = canvasGroup.direction == Direction.UP || canvasGroup.direction == Direction.DOWN ? newHeight : newWidth;
				
				for (RectTransform childRect : canvasGroup.childRects) {
					switch (canvasGroup.direction) {
						case UP:
						case DOWN:
							canvasGroup.gap -= childRect.currentSize.y;
							break;
						case LEFT:
						case RIGHT:
							canvasGroup.gap -= childRect.currentSize.x;
							break;
					}
				}
			}
		} else if (!canvasGroup.childRects.isEmpty()) {
			int gap = canvasGroup.gap;
			
			if (canvasGroup.distribution == CanvasGroup.Distribution.SPACE_BETWEEN) {
				gap = 0;
			}
			
			int totalChildWidth = 0;
			int totalChildHeight = 0;
			
			for (RectTransform childRect : canvasGroup.childRects) {
				if (childRect.absolutePosition) {
					continue;
				}
				
				totalChildWidth += childRect.currentSize.x;
				totalChildHeight += childRect.currentSize.y;
				
				switch (canvasGroup.direction) {
					case UP:
					case DOWN:
						if (childRect.size.x.getUnit() != Unit.PERCENTAGE) {
							newWidth = Math.max(newWidth, childRect.currentSize.x);
						}
						newHeight += childRect.currentSize.y + gap;
						break;
					case LEFT:
					case RIGHT:
						if (childRect.size.y.getUnit() != Unit.PERCENTAGE) {
							newWidth += childRect.currentSize.x + gap;
						}
						newHeight = Math.max(newHeight, childRect.currentSize.y);
						break;
				}
			}
			
			// Subtract gap for last element
			if (canvasGroup.direction == Direction.UP || canvasGroup.direction == Direction.DOWN) {
				newHeight -= gap;
			} else {
				newWidth -= gap;
			}
			
			if (canvasGroup.distribution == CanvasGroup.Distribution.SPACE_BETWEEN && containerRect.size != null) {
				if (canvasGroup.direction == Direction.UP || canvasGroup.direction == Direction.DOWN) {
					canvasGroup.gap = containerRect.size.computeY(containerRect) - canvasGroup.padding.computeY(containerRect) * 2 - totalChildHeight;
				} else {
					canvasGroup.gap = containerRect.size.computeX(containerRect) - canvasGroup.padding.computeX(containerRect) * 2 - totalChildWidth;
				}
			}
			
			canvasGroup.totalChildrenSize.x = totalChildWidth;
			canvasGroup.totalChildrenSize.y = totalChildHeight;
		}
		
		canvasGroup.innerSize.x = newWidth;
		canvasGroup.innerSize.y = newHeight;
		
		containerRect.currentSize.x = canvasGroup.innerSize.x + canvasGroup.padding.computeX(containerRect) * 2;
		containerRect.currentSize.y = canvasGroup.innerSize.y + canvasGroup.padding.computeY(containerRect) * 2;
	}
}
