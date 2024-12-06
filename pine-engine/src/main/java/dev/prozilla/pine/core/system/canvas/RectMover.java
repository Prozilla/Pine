package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.system.UpdateSystem;

public class RectMover extends UpdateSystem {
	
	public RectMover() {
		super( RectTransform.class);
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
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
		});
	}
}
