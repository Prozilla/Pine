package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Positions canvas elements based on their offset and anchor point inside the canvas.
 */
public class RectMover extends UpdateSystem {
	
	public RectMover() {
		super( RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		RectTransform rect = match.getComponent(RectTransform.class);
		
		if (rect.width == 0 || rect.height == 0 || rect.anchor == null) {
			return;
		}
		
		int canvasWidth = rect.getCanvas().getWidth();
		int canvasHeight = rect.getCanvas().getHeight();
		
		switch (rect.anchor) {
			case BOTTOM_LEFT:
				rect.x = rect.offsetX;
				rect.y = rect.offsetY;
				break;
			case BOTTOM_RIGHT:
				rect.x = canvasWidth - rect.width - rect.offsetX;
				rect.y = rect.offsetY;
				break;
			case BOTTOM_CENTER:
				rect.x = Math.round((float)(canvasWidth - rect.width) / 2f) + rect.offsetX;
				rect.y = rect.offsetY;
				break;
			case CENTER:
				rect.x = Math.round((float)(canvasWidth - rect.width) / 2f) + rect.offsetX;
				rect.y = Math.round((float)(canvasHeight - rect.height) / 2f) + rect.offsetY;
				break;
			case TOP_LEFT:
				rect.x = rect.offsetX;
				rect.y = canvasHeight - rect.height - rect.offsetY;
				break;
			case TOP_RIGHT:
				rect.x = canvasWidth - rect.width - rect.offsetX;
				rect.y = canvasHeight - rect.height - rect.offsetY;
				break;
			case TOP_CENTER:
				rect.x = Math.round((float)(canvasWidth - rect.width) / 2f) + rect.offsetX;
				rect.y = canvasHeight - rect.height - rect.offsetY;
				break;
		}
	}
}
