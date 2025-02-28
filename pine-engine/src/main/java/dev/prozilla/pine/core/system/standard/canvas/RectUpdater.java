package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Positions canvas elements based on their offset and anchor point inside the canvas.
 */
public class RectUpdater extends UpdateSystem {
	
	public RectUpdater() {
		super( RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		RectTransform rect = chunk.getComponent(RectTransform.class);
		updateRect(rect);
	}
	
	public static void updateRect(RectTransform rect) {
		resizeRect(rect);
		anchorRect(rect);
	}
	
	public static void resizeRect(RectTransform rect) {
		int targetSizeX = rect.getSizeX();
		int targetSizeY = rect.getSizeY();
		
		if (targetSizeX != 0) {
			rect.currentSize.x = targetSizeX;
		}
		if (targetSizeY != 0) {
			rect.currentSize.y = targetSizeY;
		}
	}
	
	public static void anchorRect(RectTransform rect) {
		if (rect.currentSize.x == 0 || rect.currentSize.y == 0 || rect.anchor == null) {
			return;
		}
		
		// Calculate remaining width and height
		int contextWidth = rect.getContext().getWidth();
		int contextHeight = rect.getContext().getHeight();
		
		int remainingWidth = contextWidth - rect.currentSize.x;
		int remainingHeight = contextHeight - rect.currentSize.y;

		// Calculate offset based on anchor and position
		float offsetX = (1 - 2 * rect.anchor.x) * rect.getPositionX();
		float offsetY = (1 - 2 * rect.anchor.y) * rect.getPositionY();
		
		rect.currentPosition.x = Math.round(rect.anchor.x * remainingWidth + offsetX);
		rect.currentPosition.y = Math.round(rect.anchor.y * remainingHeight + offsetY);
	}
}
