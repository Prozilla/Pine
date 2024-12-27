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
		
		int canvasWidth = rect.getCanvas().getWidth();
		int canvasHeight = rect.getCanvas().getHeight();
		
		switch (rect.anchor) {
			case BOTTOM_LEFT:
				rect.currentPosition.x = rect.getPositionX();
				rect.currentPosition.y = rect.getPositionY();
				break;
			case BOTTOM_RIGHT:
				rect.currentPosition.x = canvasWidth - rect.currentSize.x - rect.getPositionX();
				rect.currentPosition.y = rect.getPositionY();
				break;
			case BOTTOM_CENTER:
				rect.currentPosition.x = Math.round((float)(canvasWidth - rect.currentSize.x) / 2f) + rect.getPositionX();
				rect.currentPosition.y = rect.getPositionY();
				break;
			case CENTER:
				rect.currentPosition.x = Math.round((float)(canvasWidth - rect.currentSize.x) / 2f) + rect.getPositionX();
				rect.currentPosition.y = Math.round((float)(canvasHeight - rect.currentSize.y) / 2f) + rect.getPositionY();
				break;
			case TOP_LEFT:
				rect.currentPosition.x = rect.getPositionX();
				rect.currentPosition.y = canvasHeight - rect.currentSize.y - rect.getPositionY();
				break;
			case TOP_RIGHT:
				rect.currentPosition.x = canvasWidth - rect.currentSize.x - rect.getPositionX();
				rect.currentPosition.y = canvasHeight - rect.currentSize.y - rect.getPositionY();
				break;
			case TOP_CENTER:
				rect.currentPosition.x = Math.round((float)(canvasWidth - rect.currentSize.x) / 2f) + rect.getPositionX();
				rect.currentPosition.y = canvasHeight - rect.currentSize.y - rect.getPositionY();
				break;
		}
	}
}
