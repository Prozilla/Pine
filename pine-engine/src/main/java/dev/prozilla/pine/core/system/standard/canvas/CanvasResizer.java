package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

/**
 * Resizes canvases based on the window's dimensions.
 */
public class CanvasResizer extends UpdateSystemBase {
	
	public CanvasResizer() {
		super(CanvasRenderer.class);
	}
	
	@Override
	public void update(float deltaTime) {
		int width = application.getWindow().getWidth();
		int height = application.getWindow().getHeight();
		
		forEach(chunk -> {
			CanvasRenderer canvasRenderer = chunk.getComponent(CanvasRenderer.class);
			
			canvasRenderer.width = width;
			canvasRenderer.height = height;
		});
	}
}
