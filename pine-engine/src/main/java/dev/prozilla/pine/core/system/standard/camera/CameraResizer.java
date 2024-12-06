package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

/**
 * Resizes cameras based on the window's dimensions.
 */
public class CameraResizer extends UpdateSystemBase {
	
	public CameraResizer() {
		super(CameraData.class);
	}
	
	@Override
	public void update(float deltaTime) {
		int width = application.getWindow().getWidth();
		int height = application.getWindow().getHeight();
		
		forEach(match -> {
			CameraData cameraData = match.getComponent(CameraData.class);
			cameraData.setSize(width, height);
		});
	}
}
