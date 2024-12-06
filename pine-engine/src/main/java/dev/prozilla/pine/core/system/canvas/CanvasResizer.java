package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.system.UpdateSystem;

public class CanvasResizer extends UpdateSystem {
	
	public CanvasResizer() {
		super(CanvasRenderer.class);
	}
	
	@Override
	public void update(float deltaTime) {
		int width = application.getWindow().getWidth();
		int height = application.getWindow().getHeight();
		
		forEach(match -> {
			CanvasRenderer canvasRenderer = match.getComponent(CanvasRenderer.class);
			
			canvasRenderer.width = width;
			canvasRenderer.height = height;
		});
	}
}
