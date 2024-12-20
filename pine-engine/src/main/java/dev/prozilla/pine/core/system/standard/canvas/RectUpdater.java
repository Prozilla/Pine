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
		if (!rect.fillContainer) {
			return;
		}
		
		int canvasWidth = rect.getCanvas().getWidth();
		int canvasHeight = rect.getCanvas().getHeight();
		
		rect.size.x = canvasWidth;
		rect.size.y = canvasHeight;
	}
	
	public static void anchorRect(RectTransform rect) {
		if (rect.size.x == 0 || rect.size.y == 0 || rect.anchor == null) {
			return;
		}
		
		int canvasWidth = rect.getCanvas().getWidth();
		int canvasHeight = rect.getCanvas().getHeight();
		
		switch (rect.anchor) {
			case BOTTOM_LEFT:
				rect.position.x = rect.offset.x;
				rect.position.y = rect.offset.y;
				break;
			case BOTTOM_RIGHT:
				rect.position.x = canvasWidth - rect.size.x - rect.offset.x;
				rect.position.y = rect.offset.y;
				break;
			case BOTTOM_CENTER:
				rect.position.x = Math.round((float)(canvasWidth - rect.size.x) / 2f) + rect.offset.x;
				rect.position.y = rect.offset.y;
				break;
			case CENTER:
				rect.position.x = Math.round((float)(canvasWidth - rect.size.x) / 2f) + rect.offset.x;
				rect.position.y = Math.round((float)(canvasHeight - rect.size.y) / 2f) + rect.offset.y;
				break;
			case TOP_LEFT:
				rect.position.x = rect.offset.x;
				rect.position.y = canvasHeight - rect.size.y - rect.offset.y;
				break;
			case TOP_RIGHT:
				rect.position.x = canvasWidth - rect.size.x - rect.offset.x;
				rect.position.y = canvasHeight - rect.size.y - rect.offset.y;
				break;
			case TOP_CENTER:
				rect.position.x = Math.round((float)(canvasWidth - rect.size.x) / 2f) + rect.offset.x;
				rect.position.y = canvasHeight - rect.size.y - rect.offset.y;
				break;
		}
	}
}
